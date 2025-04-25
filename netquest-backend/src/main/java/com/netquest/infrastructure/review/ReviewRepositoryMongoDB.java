package com.netquest.infrastructure.review;

import com.netquest.domain.review.dto.ReviewAttributeClassificationDto;
import com.netquest.domain.review.dto.ReviewDto;
import com.netquest.domain.review.dto.ReviewFeedDto;
import com.netquest.domain.review.dto.ReviewCreateDto;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.*;

@Repository
public class ReviewRepositoryMongoDB {
    private final MongoCollection<Document> collection;
    private final MongoDatabase database;

    public ReviewRepositoryMongoDB(MongoClient mongoClient) {
        this.database = mongoClient.getDatabase("netQuest");
        this.collection = database.getCollection("review");
    }

    // 5.3.1 - Insert a new review
    public String insertReview(ReviewCreateDto reviewCreateDto, UUID userId) {
        Document review = new Document("_id", reviewCreateDto.getReviewId())
                .append("wifi_spot_id", reviewCreateDto.getWifiSpotId())
                .append("user_id", userId)
                .append("comment", reviewCreateDto.getReviewComment())
                .append("create_date_time", Date.from(Instant.now()))
                .append("overall_classification", reviewCreateDto.getReviewOverallClassification());

        // Handle optional attributes
        if (reviewCreateDto.getReviewAttributeClassificationDtoList() != null && !reviewCreateDto.getReviewAttributeClassificationDtoList().isEmpty()) {
            List<Document> attributesList = reviewCreateDto.getReviewAttributeClassificationDtoList().stream()
                    .map(attr -> new Document("name", attr.getName())
                            .append("value", attr.getValue()))
                    .collect(Collectors.toList());
            review.append("attributes", attributesList);
        }

        collection.insertOne(review);
        return review.get("_id").toString();
    }

    // 5.3.3 - Delete a review
    public void deleteReview(String reviewId) {
        collection.deleteOne(Filters.eq("_id", UUID.fromString(reviewId)));
    }

    // 5.3.4 - Get all reviews for a Wi-Fi spot, ordered by oldest first
    public List<ReviewDto> getReviewsForWifiSpot(UUID wifiSpotId) {
        List<Bson> pipeline = List.of(
                match(Filters.eq("wifi_spot_id", wifiSpotId)),
                lookup("users", "user_id", "_id", "user_info"),
                unwind("$user_info"),
                sort(Sorts.ascending("create_date_time"))
        );

        List<ReviewDto> reviews = new ArrayList<>();
        for (Document doc : collection.aggregate(pipeline)) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    doc.getDate("create_date_time").toInstant(),
                    ZoneId.systemDefault()
            );

            reviews.add(new ReviewDto(
                    doc.get("_id", UUID.class),
                    dateTime,
                    doc.getString("comment"),
                    doc.getInteger("overall_classification"),
                    new ArrayList<>(), // No attributes embedded
                    doc.get("wifi_spot_id", UUID.class),
                    doc.get("user_id", UUID.class),
                    doc.get("user_info", Document.class).getString("user_name")
            ));
        }

        return reviews;
    }

    // 5.3.5 - Get review feed with user and Wi-Fi spot info
    public List<ReviewFeedDto> getReviewFeed() {
        List<Bson> pipeline = List.of(
                lookup("users", "user_id", "_id", "user_info"),
                unwind("$user_info"),

                lookup("wifi_spot", "wifi_spot_id", "_id", "wifi_spot_info"),
                unwind("$wifi_spot_info"),

                lookup("wifi_spot_visit", "wifi_spot_id", "wifi_spot_id", "visits"),
                match(Filters.expr(new Document("$gt", List.of(new Document("$size", "$visits"), 0))))
        );

        List<ReviewFeedDto> reviewFeed = new ArrayList<>();
        for (Document doc : collection.aggregate(pipeline)) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    doc.getDate("create_date_time").toInstant(),
                    ZoneId.systemDefault()
            );

            List<Document> attrDocs = doc.getList("attributes", Document.class);
            List<ReviewAttributeClassificationDto> attributes = attrDocs.stream()
                    .map(attr -> new ReviewAttributeClassificationDto(
                            attr.getString("name"),
                            attr.getString("value")
                    ))
                    .collect(Collectors.toList());

            reviewFeed.add(new ReviewFeedDto(
                    doc.get("_id", UUID.class),
                    dateTime,
                    doc.getString("comment"),
                    doc.getInteger("overall_classification"),
                    attributes,
                    doc.get("user_id", UUID.class),
                    doc.get("user_info", Document.class).getString("user_name"),
                    doc.get("wifi_spot_id", UUID.class),
                    doc.get("wifi_spot_info", Document.class).getString("name")
            ));
        }

        return reviewFeed;
    }
}
