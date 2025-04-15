package com.netquest.domain.pointsearntransaction.model;


import com.netquest.domain.review.model.Review;
import com.netquest.domain.review.model.ReviewId;
import com.netquest.domain.user.model.User;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.model.WifiSpot;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisit;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "points_earn_transaction")
public class PointsEarnTransaction {

    private static final int MINUTE_VISIT_POINTS_MULTIPLIER = 5;
    private static final int POINTS_BY_WIFI_SPOT_CREATION = 600;
    private static final int POINTS_BY_REVIEW = 1000;
    private static final int MINUTE_VISIT_MY_SPOT_POINTS_MULTIPLIER = MINUTE_VISIT_POINTS_MULTIPLIER/3;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "points_earn_transaction_id"))
    })
    private PointsEarnTransactionId pointsEarnTransactionId;

    @Embedded
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "points_earn_transaction_datetime"))
    })
    private PointsEarnTransactionDateTime pointsEarnTransactionDateTime;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "points_earn_transaction_amount"))
    })
    private PointsEarnTransactionAmount pointsEarnTransactionAmount;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "points_earn_transaction_user_id"))
    })
    private UserId userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "points_earn_transaction_user_id", insertable = false, updatable = false)
    private User user;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "points_earn_transaction_wifi_spot_visit_id"))
    })
    private WifiSpotVisitId wifiSpotVisitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "points_earn_transaction_wifi_spot_visit_id", insertable = false, updatable = false)
    private WifiSpotVisit wifiSpotVisit;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "points_earn_transaction_wifi_spot_id"))
    })
    private WifiSpotId wifiSpotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "points_earn_transaction_wifi_spot_id", insertable = false, updatable = false)
    private WifiSpot wifiSpot;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "points_earn_transaction_wifi_spot_visit_my_spot_id"))
    })
    private WifiSpotVisitId wifiSpotVisitMySpotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "points_earn_transaction_wifi_spot_visit_my_spot_id", insertable = false, updatable = false)
    private WifiSpotVisit wifiSpotVisitMySpot;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "points_earn_transaction_review_id"))
    })
    private ReviewId reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "points_earn_transaction_review_id",insertable = false, updatable = false)
    private Review review;


    public static PointsEarnTransaction createPointsEarnTransactionBasedOnMyVisit(
            UserId userId,
            WifiSpotVisitId wifiSpotVisitId,
            @NonNull LocalDateTime start,
            @NonNull LocalDateTime end
    ) {
        PointsEarnTransaction pointsEarnTransaction = new PointsEarnTransaction();
        pointsEarnTransaction.pointsEarnTransactionId = new PointsEarnTransactionId();
        pointsEarnTransaction.pointsEarnTransactionDateTime = new PointsEarnTransactionDateTime();
        pointsEarnTransaction.pointsEarnTransactionAmount = calculateAmountBasedOnVisitDateTimes(start, end);
        pointsEarnTransaction.userId = new UserId(userId.getValue());
        pointsEarnTransaction.wifiSpotVisitId = new WifiSpotVisitId(wifiSpotVisitId.getValue());
        return pointsEarnTransaction;
    }

    public static PointsEarnTransaction createPointsEarnTransactionBasedOnVisitingMySpot(
            UserId userId,
            WifiSpotVisitId wifiSpotVisitId,
            @NonNull LocalDateTime start,
            @NonNull LocalDateTime end
    ) {
        PointsEarnTransaction pointsEarnTransaction = new PointsEarnTransaction();
        pointsEarnTransaction.pointsEarnTransactionId = new PointsEarnTransactionId();
        pointsEarnTransaction.pointsEarnTransactionDateTime = new PointsEarnTransactionDateTime();
        pointsEarnTransaction.pointsEarnTransactionAmount = calculateAmountBasedOnVisitMySpotDateTimes(start, end);
        pointsEarnTransaction.userId = new UserId(userId.getValue());
        pointsEarnTransaction.wifiSpotVisitMySpotId = new WifiSpotVisitId(wifiSpotVisitId.getValue());
        return pointsEarnTransaction;
    }

    public static PointsEarnTransaction createPointsEarnTransactionBasedOnReview(
            UserId userId,
            ReviewId reviewId
    ){
        PointsEarnTransaction pointsEarnTransaction = new PointsEarnTransaction();
        pointsEarnTransaction.pointsEarnTransactionId = new PointsEarnTransactionId();
        pointsEarnTransaction.pointsEarnTransactionDateTime = new PointsEarnTransactionDateTime();
        pointsEarnTransaction.pointsEarnTransactionAmount = calculateAmountBasedOnReviewCreation();
        pointsEarnTransaction.userId = new UserId(userId.getValue());
        pointsEarnTransaction.reviewId = new ReviewId(reviewId.getValue());
        return pointsEarnTransaction;
    }

    public static PointsEarnTransaction createPointsEarnTransactionBasedOnSpotCreation(UserId userId, WifiSpotId wifiSpotId) {
        PointsEarnTransaction pointsEarnTransaction = new PointsEarnTransaction();
        pointsEarnTransaction.pointsEarnTransactionId = new PointsEarnTransactionId();
        pointsEarnTransaction.pointsEarnTransactionDateTime = new PointsEarnTransactionDateTime();
        pointsEarnTransaction.pointsEarnTransactionAmount =  calculateAmountBasedOnWifiSpotCreation();
        pointsEarnTransaction.wifiSpotId = new WifiSpotId(wifiSpotId.getValue());
        pointsEarnTransaction.userId = new UserId(userId.getValue());
        return pointsEarnTransaction;
    }

    private static PointsEarnTransactionAmount calculateAmountBasedOnVisitDateTimes(LocalDateTime start, LocalDateTime end){
        long minutes = ChronoUnit.MINUTES.between(start, end);
        return new PointsEarnTransactionAmount(minutes * getVisitMinutePointsMultiplier() );
    }

    private static PointsEarnTransactionAmount calculateAmountBasedOnWifiSpotCreation(){
        return new PointsEarnTransactionAmount(getPointsByWifiSpotCreation());
    }

    private static PointsEarnTransactionAmount calculateAmountBasedOnVisitMySpotDateTimes(LocalDateTime start, LocalDateTime end){
        long minutes = ChronoUnit.MINUTES.between(start, end);
        return new PointsEarnTransactionAmount(minutes * getMinuteVisitMySpotPointsMultiplier() );
    }

    private static PointsEarnTransactionAmount calculateAmountBasedOnReviewCreation(){
        return new PointsEarnTransactionAmount(getPointsByReview());
    }

    public static int getVisitMinutePointsMultiplier(){
        return MINUTE_VISIT_POINTS_MULTIPLIER;
    }

    public static int getMinuteVisitMySpotPointsMultiplier(){
        return MINUTE_VISIT_MY_SPOT_POINTS_MULTIPLIER;
    }

    public static int getPointsByWifiSpotCreation(){
        return POINTS_BY_WIFI_SPOT_CREATION;
    }

    public static int getPointsByReview() {
        return POINTS_BY_REVIEW;
    }

}
