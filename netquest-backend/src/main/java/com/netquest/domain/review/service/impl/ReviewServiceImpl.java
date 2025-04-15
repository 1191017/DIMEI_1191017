package com.netquest.domain.review.service.impl;

import com.netquest.domain.pointsearntransaction.service.PointsEarnTransactionService;
import com.netquest.domain.review.dto.ReviewCreateDto;
import com.netquest.domain.review.dto.ReviewDto;
import com.netquest.domain.review.dto.ReviewFeedDto;
import com.netquest.domain.review.mapper.ReviewMapper;
import com.netquest.domain.review.service.ReviewService;
import com.netquest.domain.user.service.UserService;
import com.netquest.domain.wifispot.service.WifiSpotService;
import com.netquest.domain.wifispotvisit.service.WifiSpotVisitService;
import com.netquest.infrastructure.review.ReviewRepositoryCassandra;
import com.netquest.infrastructure.review.ReviewRepositoryMongoDB;
import com.netquest.infrastructure.review.ReviewRepositoryMySQL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import org.bson.Document;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepositoryMySQL reviewRepositoryMySQL;
    private final ReviewRepositoryMongoDB reviewRepositoryMongoDB;
    private final ReviewRepositoryCassandra reviewRepositoryCassandra;
    private final ReviewMapper reviewMapper;
    private final UserService userService;
    private final WifiSpotService wifiSpotService;
    private final WifiSpotVisitService wifiSpotVisitService;
    private final PointsEarnTransactionService pointsEarnTransactionService;

    // 5.3.1 - Create a new review
    @Override
    public String createReviewMongoDB(ReviewCreateDto reviewCreateDto, UUID userId) {
        return reviewRepositoryMongoDB.insertReview(reviewCreateDto, userId);
    }

    @Override
    public void deleteReviewMongoDB(String reviewId) {
        reviewRepositoryMongoDB.deleteReview(reviewId);
    }

    @Override
    public List<ReviewDto> getReviewsForWifiSpotMongoDB(UUID wifiSpotId) {
        return reviewRepositoryMongoDB.getReviewsForWifiSpot(wifiSpotId);
    }

    @Override
    public List<ReviewFeedDto> getReviewFeedMongoDB() {
        return reviewRepositoryMongoDB.getReviewFeed();
    }

    //5.3.1
    @Override
    public int createReviewMySQL(ReviewCreateDto reviewCreateDto, UUID userId) {
        return reviewRepositoryMySQL.insertReview(reviewCreateDto, userId);
    }

    //5.3.3
    @Override
    public int deleteReviewMySQL(UUID reviewId) {
        return reviewRepositoryMySQL.deleteReview(reviewId);
    }

    @Override
    // 5.3.4 - Get all reviews for a Wi-Fi spot (oldest first)
    public List<ReviewDto> getReviewsForWifiSpotMySQL(UUID wifiSpotId) {
        return reviewRepositoryMySQL.getReviewsForWifiSpot(wifiSpotId);
    }

    @Override
    // 5.3.5 - Get review feed with user and Wi-Fi Spot info
    public List<ReviewFeedDto> getReviewFeedMySQL() {
        return reviewRepositoryMySQL.getReviewFeed();
    }

    @Override
    public void createReviewCassandra(ReviewCreateDto reviewCreateDto, UUID userId) {
        reviewRepositoryCassandra.insertReview(reviewCreateDto, userId);
    }

    @Override
    public void deleteReviewCassandra(UUID reviewId) {
        reviewRepositoryCassandra.deleteReview(reviewId);
    }

    @Override
    public List<ReviewDto> getReviewsForWifiSpotCassandra(UUID wifiSpotId) {
        return reviewRepositoryCassandra.getReviewsForWifiSpot(wifiSpotId);
    }

    @Override
    public List<ReviewFeedDto> getReviewFeedCassandra() {
        return reviewRepositoryCassandra.getReviewFeed();
    }
}
