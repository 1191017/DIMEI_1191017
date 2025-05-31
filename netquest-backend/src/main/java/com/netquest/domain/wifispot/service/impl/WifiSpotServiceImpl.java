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
import java.time.temporal.TemporalAmount;
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

    public void importFromCsv(String DATA_SIZE) {
        try (BufferedReader reader = new BufferedReader(new FileReader("scripts/mysql/"+DATA_SIZE+"/wifi_spot.csv", StandardCharsets.UTF_8))) {

            String header = reader.readLine(); // skip header
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] cols = line.split(",");

                WifiSpotAddressCreateDto addressDto = new WifiSpotAddressCreateDto(
                        cols[2], "country", "zipcode", "addressline1", "address2", "city", "district"
                );

                WifiSpotCreateDto spotDto = new WifiSpotCreateDto(
                        UUID.fromString(cols[0]), "nome"+lineNumber, "descrição", 2, 3,
                        null,
                        null,
                        null,
                        null,
                        LocalTime.now(), LocalTime.now().plusHours(1),
                        true, true, true, true,
                        true, null, true,
                        true, true, true, true,
                        true, true, true, true,
                        true, true, true,
                        true, true,
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

    public void importFromCsvMongodb(String DATA_SIZE) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            int lineNumber = 0;
            List<Map<String, Object>> rawSpots = objectMapper.readValue(
                    new File("scripts/mongodb/"+DATA_SIZE+"/wifi_spot.json"), new TypeReference<>() {
                    });

            for (Map<String, Object> rawSpot : rawSpots) {
                lineNumber++;
                UUID id = UUID.fromString((String) rawSpot.get("_id"));
                UUID userId = UUID.fromString((String) rawSpot.get("user_id"));
                String name = (String) rawSpot.get("name");
                String description = "descrição";
                double latitude = 1;
                double longitude = 2;

                Map<String, Object> features = (Map<String, Object>) rawSpot.get("features");

                WifiSpotCreateDto spotDto = new WifiSpotCreateDto(
                        id,
                        name+lineNumber,
                        description,
                        latitude,
                        longitude,
                        null,
                        null,
                        null,
                        null,
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0),
                        true,
                        true,
                        true,
                        false,
                        false,
                        null,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
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
        return new WifiSpotAddressCreateDto(
                UUID.randomUUID().toString(),
                "country",
                "zip",
                "line1",
                "line2",
                "city",
                "district"
        );
    }

    public void importFromCsvCassandra(String DATA_SIZE) {
        try (BufferedReader reader = new BufferedReader(new FileReader("scripts/cassandra/"+DATA_SIZE+"/cassandra_wifi_spot.csv", StandardCharsets.UTF_8))) {

            String header = reader.readLine(); // skip header
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] c = line.split(",", -1); // keep empty values

                UUID spotId = UUID.fromString(c[0]);
                UUID userId = UUID.fromString(c[1]);
                UUID addressId = UUID.randomUUID(); // ou determinístico se quiseres

                WifiSpotAddressCreateDto addressDto = new WifiSpotAddressCreateDto(
                        addressId.toString(),
                        "country", // country
                        "zip", // zip
                        "address1", // address_line_1
                        "address2", // address_line_2
                        "city", // city
                        "district"  // district
                );

                WifiSpotCreateDto spotDto = new WifiSpotCreateDto(
                        spotId,
                        "nome"+lineNumber,  // name
                        "descrição",  // description
                        1,
                        2,
                        null,
                        null,
                        null,
                        null,
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0),
                        true, true,true,true,
                        true, null, true,
                        true, true,true,true,
                        true, true,true,true,
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

