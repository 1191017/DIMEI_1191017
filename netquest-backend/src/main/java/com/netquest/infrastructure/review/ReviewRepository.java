package com.netquest.infrastructure.review;

import com.netquest.domain.review.model.Review;
import com.netquest.domain.review.model.ReviewId;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {
    @Query("SELECT rv from Review rv " +
            "inner join WifiSpot ws on ws.wifiSpotId = rv.wifiSpotId " +
            "where rv.userId = :userId " +
            "and (:wifiSpotName is null or lower(ws.wifiSpotName.value) like lower(:wifiSpotName))" +
            "and (:startDate is null or rv.reviewCreateDateTime.value >= :startDate) " +
            "and (:endDate is null or rv.reviewCreateDateTime.value <= :endDate)")
    Optional<List<Review>> getMyReviews(UserId userId, String wifiSpotName, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT rv from Review rv " +
            "where rv.wifiSpotId = :wifiSpotId ")
    Optional<List<Review>> getReviewsOfWifiSpot(WifiSpotId wifiSpotId);
}
