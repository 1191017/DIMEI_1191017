package com.netquest.controller.wifispot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netquest.domain.wifispot.service.WifiSpotService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/wifi-spot")
public class WifiSpotController {

    private final WifiSpotService wifiSpotService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/mysql/create")
    public ResponseEntity<String> importCsv() {
        wifiSpotService.importFromCsv();
        return ResponseEntity.ok("Wi-Fi spots importados com sucesso!");
    }

    @PostMapping("/mongodb/create")
    public ResponseEntity<String> importCsvMongodb() {
        wifiSpotService.importFromCsvMongodb();
        return ResponseEntity.ok("Wi-Fi spots importados com sucesso!");
    }

    @PostMapping("/cassandra/create")
    public ResponseEntity<String> importCsvCassandra() {
        wifiSpotService.importFromCsvCassandra();
        return ResponseEntity.ok("Wi-Fi spots importados com sucesso!");
    }

    @PutMapping("/mysql/name")
    public ResponseEntity<String> updateAllNamesMySQL() {
        return bulkUpdateNames("/app/scripts/mysql/wifi_spot.csv", true, false, false);
    }

    @PutMapping("/mongodb/name")
    public ResponseEntity<String> updateAllNamesMongoDB() {
        return bulkUpdateNames("/app/scripts/mongodb/wifi_spot.json", false, true, false);
    }

    @PutMapping("/cassandra/name")
    public ResponseEntity<String> updateAllNamesCassandra() {
        return bulkUpdateNames("/app/scripts/cassandra/cassandra_wifi_spot.csv", false, false, true);
    }

    private ResponseEntity<String> bulkUpdateNames(String path, boolean mysql, boolean mongo, boolean cassandra) {
        try {
            List<Pair<UUID, String>> updates = path.endsWith(".json") ? parseJson(path) : parseCsv(path, mysql);

            wifiSpotService.updateAllWifiSpotNames(updates, mysql, mongo, cassandra);

            return ResponseEntity.ok("Updated " + updates.size() + " Wi-Fi spot names from file.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update Wi-Fi spot names: " + e.getMessage());
        }
    }

    private List<Pair<UUID, String>> parseCsv(String path, boolean mysql) throws IOException {
        List<Pair<UUID, String>> updates = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            String line;
            boolean skip = true;
            while ((line = reader.readLine()) != null) {
                if (skip) {
                    skip = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    updates.add(Pair.of(UUID.fromString(parts[0].trim()), mysql ? parts[2].trim() : parts[1].trim()));
                }
            }
        }
        return updates;
    }

    private List<Pair<UUID, String>> parseJson(String path) throws IOException {
        List<Pair<UUID, String>> updates = new ArrayList<>();
        JsonNode root = objectMapper.readTree(Files.newBufferedReader(Paths.get(path)));
        for (JsonNode node : root) {
            UUID id = UUID.fromString(node.get("_id").asText());
            String name = node.get("name").asText();
            updates.add(Pair.of(id, name));
        }
        return updates;
    }
}
