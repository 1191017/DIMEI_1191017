package com.netquest.infrastructure.review;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.netquest.domain.review.dto.ReviewDto;
import com.netquest.domain.review.dto.ReviewCreateDto;
import com.netquest.domain.review.dto.ReviewFeedDto;
import com.netquest.domain.review.dto.ReviewAttributeClassificationDto;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ReviewRepositoryCassandra {
    private final CqlSession session;

    public ReviewRepositoryCassandra(CqlSession session) {
        this.session = session;
    }

    // 5.3.1 - Insert a new review with attributes
    public void insertReview(ReviewCreateDto reviewCreateDto, UUID userId) {
        UUID reviewId = reviewCreateDto.getReviewId();
        Instant timestamp = Instant.now();

        String reviewQuery = """
            INSERT INTO review (wifi_spot_id, review_id, user_id, review_comment, review_create_date_time, review_overall_classification)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement reviewStmt = session.prepare(reviewQuery);
        session.execute(reviewStmt.bind(
                reviewCreateDto.getWifiSpotId(), reviewId, userId,
                reviewCreateDto.getReviewComment(), timestamp,
                reviewCreateDto.getReviewOverallClassification()
        ));

        // Insert into review_attributes table
        if (reviewCreateDto.getReviewAttributeClassificationDtoList() != null) {
            String attributeQuery = """
                INSERT INTO review_attribute_classification (review_id, attribute_name, attribute_value) VALUES (?, ?, ?)
            """;
            PreparedStatement attrStmt = session.prepare(attributeQuery);
            for (var attr : reviewCreateDto.getReviewAttributeClassificationDtoList()) {
                session.execute(attrStmt.bind(reviewId, attr.getName(), attr.getValue()));
            }
        }
    }

    // 5.3.3 - Delete a review and its attributes
    public void deleteReview(UUID reviewId) {
        // Delete attributes first
        String deleteAttributesQuery = "DELETE FROM review_attribute_classification WHERE review_id = ?";
        session.execute(session.prepare(deleteAttributesQuery).bind(reviewId));

        // Delete review
        String deleteReviewQuery = "DELETE FROM review WHERE review_id = ?";
        session.execute(session.prepare(deleteReviewQuery).bind(reviewId));
    }

    // 5.3.4 - Get all reviews for a Wi-Fi spot (ordered by oldest first)
    public List<ReviewDto> getReviewsForWifiSpot(UUID wifiSpotId) {
        String reviewQuery = """
            SELECT review_id, review_create_date_time, review_comment, review_overall_classification, user_id
            FROM review WHERE wifi_spot_id = ?
        """;

        PreparedStatement reviewStmt = session.prepare(reviewQuery);
        ResultSet rs = session.execute(reviewStmt.bind(wifiSpotId));

        List<ReviewDto> reviews = new ArrayList<>();
        for (Row row : rs) {
            UUID reviewId = row.getUuid("review_id");

            // Fetch attributes for each review
            List<ReviewAttributeClassificationDto> attributes = getReviewAttributes(reviewId);

            reviews.add(new ReviewDto(
                    reviewId,
                    row.getInstant("review_create_date_time").atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                    row.getString("review_comment"),
                    row.getInt("review_overall_classification"),
                    attributes,
                    wifiSpotId,
                    row.getUuid("user_id"),
                    "Unknown"
            ));
        }

        return reviews.stream()
                .sorted(Comparator.comparing(ReviewDto::getReviewCreateDateTime).reversed())
                .collect(Collectors.toList());
    }

    // 5.3.5 - Get review feed (fetch reviews first, attributes separately)
    public List<ReviewFeedDto> getReviewFeed() {
        String reviewQuery = """
        SELECT wifi_spot_id, review_id, review_create_date_time, review_comment, review_overall_classification, user_id
        FROM review
    """;

        ResultSet rs = session.execute(session.prepare(reviewQuery).bind());

        List<ReviewFeedDto> reviewFeed = new ArrayList<>();
        for (Row row : rs) {
            UUID wifiSpotId = row.getUuid("wifi_spot_id");

            if (!wasWifiSpotVisited(wifiSpotId)) continue;

            UUID userId = row.getUuid("user_id");
            UUID reviewId = row.getUuid("review_id");

            String username = getUsername(userId);
            String wifiSpotName = getWifiSpotName(wifiSpotId);

            List<ReviewAttributeClassificationDto> attributes = getReviewAttributes(reviewId);

            reviewFeed.add(new ReviewFeedDto(
                    reviewId,
                    row.getInstant("review_create_date_time").atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    row.getString("review_comment"),
                    row.getInt("review_overall_classification"),
                    attributes,
                    userId,
                    username,
                    wifiSpotId,
                    wifiSpotName
            ));
        }
        return reviewFeed;
    }

    private boolean wasWifiSpotVisited(UUID wifiSpotId) {
        String visitQuery = "SELECT COUNT(*) FROM wifi_spot_visit WHERE wifi_spot_id = ?";
        ResultSet rs = session.execute(session.prepare(visitQuery).bind(wifiSpotId));
        return rs.one().getLong(0) > 0;
    }

    private String getUsername(UUID userId) {
        String userQuery = "SELECT user_name FROM users WHERE user_id = ?";
        ResultSet rs = session.execute(session.prepare(userQuery).bind(userId));
        Row row = rs.one();
        return row != null ? row.getString("username") : "Unknown";
    }

    private String getWifiSpotName(UUID wifiSpotId) {
        String spotQuery = "SELECT wifi_spot_name FROM wifi_spot WHERE wifi_spot_id = ?";
        ResultSet rs = session.execute(session.prepare(spotQuery).bind(wifiSpotId));
        Row row = rs.one();
        return row != null ? row.getString("name") : "Unknown";
    }

    // Fetch review attributes separately
    private List<ReviewAttributeClassificationDto> getReviewAttributes(UUID reviewId) {
        String attributeQuery = "SELECT attribute_name, attribute_value FROM review_attribute_classification WHERE review_id = ?";
        PreparedStatement stmt = session.prepare(attributeQuery);
        ResultSet rs = session.execute(stmt.bind(reviewId));

        List<ReviewAttributeClassificationDto> attributes = new ArrayList<>();
        for (Row row : rs) {
            attributes.add(new ReviewAttributeClassificationDto(
                    row.getString("attribute_name"),
                    row.getString("attribute_value")
            ));
        }
        return attributes;
    }
}
