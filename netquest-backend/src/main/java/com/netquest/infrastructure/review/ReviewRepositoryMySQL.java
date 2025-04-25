package com.netquest.infrastructure.review;

import com.netquest.domain.review.dto.ReviewAttributeClassificationDto;
import com.netquest.domain.review.dto.ReviewCreateDto;
import com.netquest.domain.review.dto.ReviewDto;
import com.netquest.domain.review.dto.ReviewFeedDto;
import com.netquest.domain.review.mapper.ReviewMapper;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ReviewRepositoryMySQL {
    private final String url = "jdbc:mysql://localhost:3306/netQuest";
    private final String username = "netQuest";
    private final String password = "netQuestLocal";

    public ReviewRepositoryMySQL() {}

    // 5.3.1 - Insert a new review with attributes
    public int insertReview(ReviewCreateDto reviewCreateDto, UUID userId) {
        UUID reviewId = reviewCreateDto.getReviewId();
        String reviewSql = """
            INSERT INTO review (review_id, review_comment, review_create_date_time, review_overall_classification, review_user_id, review_wifi_spot_id)
            VALUES (UUID_TO_BIN(?), ?, NOW(), ?, UUID_TO_BIN(?), UUID_TO_BIN(?))
        """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement reviewStmt = connection.prepareStatement(reviewSql)) {

            reviewStmt.setString(1, reviewId.toString());
            reviewStmt.setString(2, reviewCreateDto.getReviewComment());
            reviewStmt.setInt(3, reviewCreateDto.getReviewOverallClassification());
            reviewStmt.setString(4, userId.toString());
            reviewStmt.setString(5, reviewCreateDto.getWifiSpotId().toString());

            int rowsAffected = reviewStmt.executeUpdate();

            // Insert attributes if they exist
            if (reviewCreateDto.getReviewAttributeClassificationDtoList() != null) {
                String attributeSql = """
                    INSERT INTO review_attribute_classification (review_id, review_attribute_classification_name, review_attribute_classification_value)
                    VALUES (UUID_TO_BIN(?), ?, ?)
                """;
                try (PreparedStatement attributeStmt = connection.prepareStatement(attributeSql)) {
                    for (ReviewAttributeClassificationDto attr : reviewCreateDto.getReviewAttributeClassificationDtoList()) {
                        attributeStmt.setString(1, reviewId.toString());
                        attributeStmt.setString(2, attr.getName());
                        attributeStmt.setString(3, attr.getValue());
                        attributeStmt.executeUpdate();
                    }
                }
            }

            return rowsAffected;
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting review", e);
        }
    }

    // 5.3.3 - Delete a review and its attributes
    public int deleteReview(UUID reviewId) {
        String deleteAttributesSql = "DELETE FROM review_attribute_classification WHERE review_id = UUID_TO_BIN(?)";
        String deleteReviewSql = "DELETE FROM review WHERE review_id = UUID_TO_BIN(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement attrStmt = connection.prepareStatement(deleteAttributesSql);
             PreparedStatement reviewStmt = connection.prepareStatement(deleteReviewSql)) {

            attrStmt.setString(1, reviewId.toString());
            attrStmt.executeUpdate();

            reviewStmt.setString(1, reviewId.toString());
            return reviewStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting review", e);
        }
    }

    // 5.3.4 - Get all reviews for a Wi-Fi spot, ordered by oldest first
    public List<ReviewDto> getReviewsForWifiSpot(UUID wifiSpotId) {
        String sql = """
        SELECT 
            BIN_TO_UUID(r.review_id) AS review_id, r.review_comment, r.review_create_date_time, r.review_overall_classification,
            BIN_TO_UUID(r.review_wifi_spot_id) AS wifi_spot_id, BIN_TO_UUID(r.review_user_id) AS user_id, u.user_name
        FROM review r
        JOIN users u ON r.review_user_id = u.user_id
        WHERE r.review_wifi_spot_id = UUID_TO_BIN(?)
        ORDER BY r.review_create_date_time ASC
    """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, wifiSpotId.toString());
            ResultSet rs = stmt.executeQuery();

            List<ReviewDto> reviews = new ArrayList<>();
            while (rs.next()) {
                reviews.add(new ReviewDto(
                        UUID.fromString(rs.getString("review_id")),
                        rs.getTimestamp("review_create_date_time").toLocalDateTime(),
                        rs.getString("review_comment"),
                        rs.getInt("review_overall_classification"),
                        new ArrayList<>(), // Empty attribute list for now
                        UUID.fromString(rs.getString("wifi_spot_id")),
                        UUID.fromString(rs.getString("user_id")),
                        rs.getString("user_name")
                ));
            }
            return reviews;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching reviews", e);
        }
    }

    // 5.3.5 - Complex Select: Get review feed with user and Wi-Fi Spot info
    public List<ReviewFeedDto> getReviewFeed() {
        String sql = """
        SELECT 
            BIN_TO_UUID(r.review_id) AS review_id,
            r.review_comment,
            r.review_create_date_time,
            r.review_overall_classification,
            BIN_TO_UUID(u.user_id) AS user_id,
            u.user_name,
            u.mail,
            BIN_TO_UUID(w.wifi_spot_id) AS wifi_spot_id,
            w.wifi_spot_name,
            w.wifi_spot_description
        FROM review r
        JOIN users u ON r.review_user_id = u.user_id
        JOIN wifi_spot w ON r.review_wifi_spot_id = w.wifi_spot_id
        JOIN wifi_spot_visit v ON r.review_wifi_spot_id = v.wifi_spot_id
        GROUP BY r.review_id 
        ORDER BY r.review_create_date_time DESC
    """;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<ReviewFeedDto> feed = new ArrayList<>();
            while (rs.next()) {
                feed.add(new ReviewFeedDto(
                        UUID.fromString(rs.getString("review_id")),
                        rs.getTimestamp("review_create_date_time").toLocalDateTime(),
                        rs.getString("review_comment"),
                        rs.getInt("review_overall_classification"),
                        getReviewAttributes(rs.getString("review_id")), // assuming this is done separately
                        UUID.fromString(rs.getString("user_id")),
                        rs.getString("user_name"),
                        UUID.fromString(rs.getString("wifi_spot_id")),
                        rs.getString("wifi_spot_name")
                ));
            }
            return feed;
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching review feed", e);
        }
    }

    private List<ReviewAttributeClassificationDto> getReviewAttributes(String reviewId) {
        String sql = "SELECT review_attribute_classification_name, review_attribute_classification_value FROM review_attribute_classification WHERE review_id = UUID_TO_BIN(?)";
        List<ReviewAttributeClassificationDto> attributes = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reviewId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                attributes.add(new ReviewAttributeClassificationDto(
                        rs.getString("review_attribute_classification_name"),
                        rs.getString("review_attribute_classification_value")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching review attributes", e);
        }

        return attributes;
    }
}
