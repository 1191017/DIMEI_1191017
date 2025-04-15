package com.netquest.domain.review.service;

import com.netquest.domain.review.dto.ReviewCreateDto;
import com.netquest.domain.review.dto.ReviewDto;
import com.netquest.domain.review.dto.ReviewFeedDto;
import com.netquest.domain.review.dto.ReviewHistoryDto;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitHistoryDto;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReviewService {
    List<ReviewFeedDto> getReviewFeedMongoDB();

    List<ReviewDto> getReviewsForWifiSpotMongoDB(UUID wifiSpotId);

    void deleteReviewMongoDB(String reviewId);

    String createReviewMongoDB(ReviewCreateDto reviewCreateDto, UUID userId);

    int createReviewMySQL(ReviewCreateDto reviewCreateDto, UUID userId);

    int deleteReviewMySQL(UUID reviewId);

    List<ReviewDto> getReviewsForWifiSpotMySQL(UUID wifiSpotId);

    List<ReviewFeedDto> getReviewFeedMySQL();

    void createReviewCassandra(ReviewCreateDto reviewCreateDto, UUID userId);
    void deleteReviewCassandra(UUID reviewId);
    List<ReviewDto> getReviewsForWifiSpotCassandra(UUID wifiSpotId);
    List<ReviewFeedDto> getReviewFeedCassandra();
}
