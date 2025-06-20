package com.netquest.controller.review;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netquest.domain.review.dto.ReviewAttributeClassificationDto;
import com.netquest.domain.review.dto.ReviewCreateDto;
import com.netquest.domain.review.dto.ReviewDto;
import com.netquest.domain.review.dto.ReviewFeedDto;
import com.netquest.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    //MongoDB

    // 5.3.1 - Create a new review
    @PostMapping("/mongodb/file-create/{DATA_SIZE}")
    public ResponseEntity<String> createReviewsMongoDBFromFile(@PathVariable("DATA_SIZE") String dataSize) {
        try {
            Path path = Paths.get("scripts/mongodb/"+dataSize+"/review.json");

            String jsonContent = Files.readString(path);
            List<Map<String, Object>> reviews = objectMapper.readValue(jsonContent, new TypeReference<>() {});
            List<ReviewCreateDto> dtos = new ArrayList<>();

            for (Map<String, Object> review : reviews) {
                List<Map<String, String>> attrList = (List<Map<String, String>>) review.get("attributes");
                List<ReviewAttributeClassificationDto> mappedAttributes = attrList.stream()
                        .map(a -> new ReviewAttributeClassificationDto(a.get("name"), "low"))
                        .collect(Collectors.toList());

                ReviewCreateDto dto = new ReviewCreateDto();
                dto.setReviewId(UUID.fromString((String) review.get("_id")));
                dto.setWifiSpotId(UUID.fromString((String) review.get("wifi_spot_id")));
                dto.setReviewComment("comentário");
                dto.setReviewOverallClassification(1);
                dto.setReviewAttributeClassificationDtoList(mappedAttributes);

                dtos.add(dto);
            }

            for (ReviewCreateDto dto : dtos) {
                reviewService.createReviewMongoDB(dto, UUID.fromString((String) reviews.get(dtos.indexOf(dto)).get("user_id")));
            }

            return ResponseEntity.ok("Created " + dtos.size() + " reviews in MongoDB.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/mongodb/file-delete/{DATA_SIZE}")
    public ResponseEntity<String> deleteReviewsMongoDBFromFile(@PathVariable("DATA_SIZE") String dataSize) {
        try {
            Path path = Paths.get("scripts/mongodb/"+dataSize+"/review.json");

            // Read file content into a String
            String jsonContent = Files.readString(path);
            List<Map<String, Object>> reviews = objectMapper.readValue(jsonContent, new TypeReference<>() {});
            List<String> ids = reviews.stream()
                    .map(r -> (String) r.get("_id"))
                    .collect(Collectors.toList());

            for (String id : ids) {
                reviewService.deleteReviewMongoDB(id);
            }

            return ResponseEntity.ok("Deleted " + ids.size() + " reviews from MongoDB.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/mongodb/wifiSpot/{wifiSpotId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForWifiSpotMongoDB(@PathVariable String wifiSpotId) {
        return ResponseEntity.ok(reviewService.getReviewsForWifiSpotMongoDB(UUID.fromString(wifiSpotId)));
    }

    @GetMapping("/mongodb/feed")
    public ResponseEntity<List<ReviewFeedDto>> getReviewFeedMongoDB() {
        return ResponseEntity.ok(reviewService.getReviewFeedMongoDB());
    }

    //MySQL
    // 5.3.1 - Create reviews from file
    @PostMapping("/mysql/file-create/{DATA_SIZE}")
    public ResponseEntity<String> createReviewsMySQLFromFile(@PathVariable("DATA_SIZE") String dataSize) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("scripts/mysql/"+dataSize+"/review.csv"));
            lines.remove(0);

            Map<String, List<ReviewAttributeClassificationDto>> attributesMap = Files.readAllLines(Paths.get("scripts/mysql/"+dataSize+"/review_attribute_classification.csv"))
                    .stream()
                    .skip(1)
                    .map(line -> line.split(","))
                    .collect(Collectors.groupingBy(
                            parts -> parts[0],
                            Collectors.mapping(
                                    parts -> new ReviewAttributeClassificationDto(parts[1], "low"),
                                    Collectors.toList()
                            )
                    ));

            List<ReviewCreateDto> dtos = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                String reviewId = parts[0];
                List<ReviewAttributeClassificationDto> attrList = attributesMap.getOrDefault(reviewId, new ArrayList<>());

                ReviewCreateDto dto = new ReviewCreateDto();
                dto.setReviewId(UUID.fromString(reviewId));
                dto.setWifiSpotId(UUID.fromString(parts[2]));
                dto.setReviewComment("comentário");
                dto.setReviewOverallClassification(1);
                dto.setReviewAttributeClassificationDtoList(attrList);

                dtos.add(dto);
            }

            for (ReviewCreateDto dto : dtos) {
                reviewService.createReviewMySQL(dto, UUID.fromString(lines.get(dtos.indexOf(dto)).split(",")[1]));
            }

            return ResponseEntity.ok("Created " + dtos.size() + " reviews in MySQL.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/mysql/file-delete/{DATA_SIZE}")
    public ResponseEntity<String> deleteReviewsMySQLFromFile(@PathVariable("DATA_SIZE") String dataSize) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("scripts/mysql/"+dataSize+"/review.csv"));
            lines.remove(0);
            List<UUID> ids = lines.stream()
                    .map(l -> UUID.fromString(l.split(",")[0]))
                    .collect(Collectors.toList());

            for (UUID id : ids) {
                reviewService.deleteReviewMySQL(id);
            }

            return ResponseEntity.ok("Deleted " + ids.size() + " reviews from MySQL.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/mysql/wifiSpot/{wifiSpotId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForWifiSpotMySQL(@PathVariable String wifiSpotId) {
        return ResponseEntity.ok(reviewService.getReviewsForWifiSpotMySQL(UUID.fromString(wifiSpotId)));
    }

    @GetMapping("/mysql/feed")
    public ResponseEntity<List<ReviewFeedDto>> getReviewFeedMySQL() {
        return ResponseEntity.ok(reviewService.getReviewFeedMySQL());
    }

    //CASSANDRA

    @PostMapping("/cassandra/file-create/{DATA_SIZE}")
    public ResponseEntity<String> createReviewsCassandraFromFile(@PathVariable("DATA_SIZE") String dataSize) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("scripts/cassandra/"+dataSize+"/cassandra_review.csv"));
            lines.remove(0);

            Map<String, List<ReviewAttributeClassificationDto>> attributesMap = Files.readAllLines(Paths.get("scripts/cassandra/"+dataSize+"/cassandra_review_attribute_classification.csv"))
                    .stream()
                    .skip(1)
                    .map(line -> line.split(","))
                    .collect(Collectors.groupingBy(
                            parts -> parts[0],
                            Collectors.mapping(
                                    parts -> new ReviewAttributeClassificationDto(parts[1], "low"),
                                    Collectors.toList()
                            )
                    ));

            List<ReviewCreateDto> dtos = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                String reviewId = parts[1];
                List<ReviewAttributeClassificationDto> attrList = attributesMap.getOrDefault(reviewId, new ArrayList<>());

                ReviewCreateDto dto = new ReviewCreateDto();
                dto.setReviewId(UUID.fromString(reviewId));
                dto.setWifiSpotId(UUID.fromString(parts[0]));
                dto.setReviewComment("comentário");
                dto.setReviewOverallClassification(1);
                dto.setReviewAttributeClassificationDtoList(attrList);

                dtos.add(dto);
            }

            for (ReviewCreateDto dto : dtos) {
                reviewService.createReviewCassandra(dto, UUID.fromString(lines.get(dtos.indexOf(dto)).split(",")[2]));
            }

            return ResponseEntity.ok("Created " + dtos.size() + " reviews in Cassandra.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/cassandra/file-delete/{DATA_SIZE}")
    public ResponseEntity<String> deleteReviewsCassandraFromFile(@PathVariable("DATA_SIZE") String dataSize) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("scripts/cassandra/"+dataSize+"/cassandra_review.csv"));
            lines.remove(0);
            List<UUID> ids = lines.stream()
                    .map(l -> UUID.fromString(l.split(",")[1]))
                    .collect(Collectors.toList());

            for (UUID id : ids) {
                reviewService.deleteReviewCassandra(id);
            }

            return ResponseEntity.ok("Deleted " + ids.size() + " reviews from Cassandra.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/cassandra/wifiSpot/{wifiSpotId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForWifiSpotCassandra(@PathVariable String wifiSpotId) {
        return ResponseEntity.ok(reviewService.getReviewsForWifiSpotCassandra(UUID.fromString(wifiSpotId)));
    }

    @GetMapping("/cassandra/feed")
    public ResponseEntity<List<ReviewFeedDto>> getReviewFeedCassandra() {
        return ResponseEntity.ok(reviewService.getReviewFeedCassandra());
    }
}
