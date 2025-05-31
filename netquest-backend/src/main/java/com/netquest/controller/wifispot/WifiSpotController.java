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

    @PostMapping("/mysql/create/{DATA_SIZE}")
    public ResponseEntity<String> importCsv(@PathVariable("DATA_SIZE") String dataSize) {
        wifiSpotService.importFromCsv(dataSize);
        return ResponseEntity.ok("Wi-Fi spots importados com sucesso!");
    }

    @PostMapping("/mongodb/create/{DATA_SIZE}")
    public ResponseEntity<String> importCsvMongodb(@PathVariable("DATA_SIZE") String dataSize) {
        wifiSpotService.importFromCsvMongodb(dataSize);
        return ResponseEntity.ok("Wi-Fi spots importados com sucesso!");
    }

    @PostMapping("/cassandra/create/{DATA_SIZE}")
    public ResponseEntity<String> importCsvCassandra(@PathVariable("DATA_SIZE") String dataSize) {
        wifiSpotService.importFromCsvCassandra(dataSize);
        return ResponseEntity.ok("Wi-Fi spots importados com sucesso!");
    }

    @PutMapping("/mysql/name/{DATA_SIZE}")
    public ResponseEntity<String> updateAllNamesMySQL(@PathVariable("DATA_SIZE") String dataSize) {
        return bulkUpdateNames("scripts/mysql/"+dataSize+"/wifi_spot.csv", true, false, false);
    }

    @PutMapping("/mongodb/name/{DATA_SIZE}")
    public ResponseEntity<String> updateAllNamesMongoDB(@PathVariable("DATA_SIZE") String dataSize) {
        return bulkUpdateNames("scripts/mongodb/"+dataSize+"/wifi_spot.json", false, true, false);
    }

    @PutMapping("/cassandra/name/{DATA_SIZE}")
    public ResponseEntity<String> updateAllNamesCassandra(@PathVariable("DATA_SIZE") String dataSize) {
        return bulkUpdateNames("scripts/cassandra/"+dataSize+"/cassandra_wifi_spot.csv", false, false, true);
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
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (skip) {
                    skip = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    updates.add(Pair.of(UUID.fromString(parts[0].trim()), "nomeupdated"+lineNumber));
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
