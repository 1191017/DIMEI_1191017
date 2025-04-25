package com.netquest.domain.wifispot.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.netquest.domain.wifispot.dto.WifiSpotAddressCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotCreateDto;
import com.netquest.domain.wifispot.model.WifiSpot;
import com.netquest.domain.wifispot.model.WifiSpotAddress;
import com.netquest.domain.wifispot.service.WifiSpotService;
import com.netquest.infrastructure.wifispot.WifiSpotRepositoryCassandra;
import com.netquest.infrastructure.wifispot.WifiSpotRepositoryMongoDB;
import com.netquest.infrastructure.wifispot.WifiSpotRepositoryMySQL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;

@RequiredArgsConstructor
@Service
public class WifiSpotServiceImpl implements WifiSpotService {
    private final WifiSpotRepositoryMySQL wifiSpotRepositoryMySQL;
    private final WifiSpotRepositoryMongoDB wifiSpotRepositoryMongoDB;
    private final WifiSpotRepositoryCassandra wifiSpotRepositoryCassandra;

    // 5.3.2 - Update Wi-Fi spot description
    private void updateWifiSpotName(UUID wifiSpotId, String newName, boolean mysql, boolean mongodb, boolean cassandra) {
        if (mysql) {
            wifiSpotRepositoryMySQL.updateWifiSpotName(wifiSpotId, newName);
        } else if (mongodb) {
            wifiSpotRepositoryMongoDB.updateWifiSpotName(wifiSpotId, newName);
        }
        else if(cassandra){
            wifiSpotRepositoryCassandra.updateWifiSpotName(wifiSpotId, newName);
        }
    }

    @Override
    public void updateAllWifiSpotNames(List<Pair<UUID, String>> updates, boolean mysql, boolean mongodb, boolean cassandra) {
        for (Pair<UUID, String> entry : updates) {
            updateWifiSpotName(entry.getLeft(), entry.getRight(), mysql, mongodb, cassandra);
        }
    }

    public void importFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader("scripts/mysql/wifi_spot.csv", StandardCharsets.UTF_8))) {

            String header = reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",");

                WifiSpotAddressCreateDto addressDto = new WifiSpotAddressCreateDto(
                        cols[34], cols[2], cols[6], cols[4], cols[5], cols[3], cols[1]
                );

                WifiSpotCreateDto spotDto = new WifiSpotCreateDto(
                        UUID.fromString(cols[0]), cols[2], cols[3], Double.parseDouble(cols[4]), Double.parseDouble(cols[5]),
                        null,
                        null,
                        null,
                        null,
                        LocalTime.parse(cols[25]), LocalTime.parse(cols[26]),
                        parseBoolean(cols[10]), parseBoolean(cols[9]), parseBoolean(cols[7]), parseBoolean(cols[12]),
                        parseBoolean(cols[11]), null, parseBoolean(cols[14]),
                        parseBoolean(cols[8]), parseBoolean(cols[6]), parseBoolean(cols[15]), parseBoolean(cols[16]),
                        parseBoolean(cols[20]), parseBoolean(cols[19]), parseBoolean(cols[18]), parseBoolean(cols[17]),
                        parseBoolean(cols[24]), parseBoolean(cols[23]), parseBoolean(cols[30]),
                        parseBoolean(cols[31]), parseBoolean(cols[32]),
                        addressDto, null
                );

                wifiSpotRepositoryMySQL.createWifiSpot(UUID.fromString(cols[1]), spotDto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro a importar CSV: " + e.getMessage(), e);
        }
    }

    private boolean parseBoolean(String s) {
        return s.trim().equals("1") || s.equalsIgnoreCase("true");
    }

    public void importFromCsvMongodb() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map<String, Object>> rawSpots = objectMapper.readValue(
                    new File("scripts/mongodb/wifi_spot.json"), new TypeReference<>() {
                    });

            for (Map<String, Object> rawSpot : rawSpots) {
                UUID id = UUID.fromString((String) rawSpot.get("_id"));
                UUID userId = UUID.fromString((String) rawSpot.get("user_id"));
                String name = (String) rawSpot.get("name");
                String description = (String) rawSpot.get("description");
                double latitude = ((Number) rawSpot.get("latitude")).doubleValue();
                double longitude = ((Number) rawSpot.get("longitude")).doubleValue();

                Map<String, Object> features = (Map<String, Object>) rawSpot.get("features");

                WifiSpotCreateDto spotDto = new WifiSpotCreateDto(
                        id,
                        name,
                        description,
                        latitude,
                        longitude,
                        null,
                        null,
                        null,
                        null,
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0),
                        (Boolean) features.get("crowded"),
                        (Boolean) features.get("covered_area"),
                        (Boolean) features.get("air_conditioning"),
                        false,
                        false,
                        null,
                        (Boolean) features.get("pet_friendly"),
                        (Boolean) features.get("child_friendly"),
                        (Boolean) features.get("disabled_access"),
                        (Boolean) features.get("available_power_outlets"),
                        (Boolean) features.get("charging_stations"),
                        (Boolean) features.get("restrooms_available"),
                        (Boolean) features.get("parking_availability"),
                        (Boolean) features.get("food_options"),
                        (Boolean) features.get("drink_options"),
                        (Boolean) features.get("open_during_rain"),
                        (Boolean) features.get("open_during_heat"),
                        (Boolean) features.get("heated_in_winter"),
                        (Boolean) features.get("shaded_areas"),
                        (Boolean) features.get("outdoor_fans"),
                        parseAddressDto(rawSpot),
                        null
                );

                wifiSpotRepositoryMongoDB.createWifiSpot(spotDto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar JSON: " + e.getMessage(), e);
        }
    }

    private WifiSpotAddressCreateDto parseAddressDto(Map<String, Object> rawSpot) {
        Map<String, Object> addr = (Map<String, Object>) rawSpot.get("address");
        return new WifiSpotAddressCreateDto(
                UUID.randomUUID().toString(),
                (String) addr.get("country"),
                (String) addr.get("zip_code"),
                (String) addr.get("line1"),
                (String) addr.get("line2"),
                (String) addr.get("city"),
                (String) addr.get("district")
        );
    }

    public void importFromCsvCassandra() {
        try (BufferedReader reader = new BufferedReader(new FileReader("scripts/cassandra/cassandra_wifi_spot.csv", StandardCharsets.UTF_8))) {

            String header = reader.readLine(); // skip header
            String line;

            while ((line = reader.readLine()) != null) {
                String[] c = line.split(",", -1); // keep empty values

                UUID spotId = UUID.fromString(c[0]);
                UUID userId = UUID.fromString(c[24]);
                UUID addressId = UUID.randomUUID(); // ou determinístico se quiseres

                WifiSpotAddressCreateDto addressDto = new WifiSpotAddressCreateDto(
                        addressId.toString(),
                        c[31], // country
                        c[32], // zip
                        c[25], // address_line_1
                        c[26], // address_line_2
                        c[27], // city
                        c[28]  // district
                );

                WifiSpotCreateDto spotDto = new WifiSpotCreateDto(
                        spotId,
                        c[1],  // name
                        c[4],  // description
                        Double.parseDouble(c[2]),
                        Double.parseDouble(c[3]),
                        null,
                        null,
                        null,
                        null,
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0),
                        toBool(c[5]), toBool(c[6]), toBool(c[7]), toBool(c[10]),
                        toBool(c[9]), null, toBool(c[19]),
                        toBool(c[8]), toBool(c[18]), toBool(c[17]), toBool(c[16]),
                        toBool(c[21]), toBool(c[20]), toBool(c[22]), toBool(c[23]),
                        false, false, false, false, false, // campos adicionais opcionais
                        addressDto,
                        null // ou outro índice se necessário
                );

                wifiSpotRepositoryCassandra.createWifiSpot(spotDto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar CSV: " + e.getMessage(), e);
        }
    }

    private boolean toBool(String s) {
        return s.trim().equalsIgnoreCase("true") || s.trim().equals("1");
    }

    private int toBit(String s) {
        return toBool(s) ? 1 : 0;
    }
}

