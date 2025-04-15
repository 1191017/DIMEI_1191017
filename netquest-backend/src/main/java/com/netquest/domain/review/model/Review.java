package com.netquest.domain.review.model;

import com.netquest.domain.user.model.User;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.model.WifiSpot;
import com.netquest.domain.wifispot.model.WifiSpotId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "review_id"))
    })
    private ReviewId reviewId;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "review_create_date_time"))
    })
    private ReviewCreateDateTime reviewCreateDateTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "review_comment"))
    })
    private ReviewComment reviewComment;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "review_overall_classification"))
    })
    private ReviewOverallClassification reviewOverallClassification;

    @ElementCollection
    @CollectionTable(
            name = "review_attribute_classification",
            joinColumns = @JoinColumn(name = "review_id")
    )
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "review_attribute_classification_name")),
            @AttributeOverride(name = "value", column = @Column(name = "review_attribute_classification_value"))
    })
    private List<ReviewAttributeClassification> reviewAttributeClassificationList;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "review_wifi_spot_id"))
    })
    private WifiSpotId wifiSpotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_wifi_spot_id", insertable = false, updatable = false)
    private WifiSpot wifiSpot;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "review_user_id"))
    })
    private UserId userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_user_id", insertable = false, updatable = false)
    private User user;


    public Review(@NonNull ReviewId reviewId, @NonNull ReviewCreateDateTime reviewCreateDateTime,
                  ReviewComment reviewComment, @NonNull ReviewOverallClassification reviewOverallClassification,
                  List<ReviewAttributeClassification> reviewAttributeClassificationList
                ,@NonNull WifiSpotId wifiSpotId
                ,@NonNull UserId userId) {
        this.reviewId = new ReviewId(reviewId.getValue());
        this.reviewCreateDateTime = new ReviewCreateDateTime(reviewCreateDateTime.getValue());
        this.reviewComment = (reviewComment != null ?  new ReviewComment(reviewComment.getValue()) : new ReviewComment());
        this.reviewOverallClassification =  new ReviewOverallClassification(reviewOverallClassification.getValue());
        this.reviewAttributeClassificationList = (reviewAttributeClassificationList !=null ? new ArrayList<>(reviewAttributeClassificationList) : new ArrayList<>());
        this.wifiSpotId = new WifiSpotId(wifiSpotId.getValue());
        this.userId = new UserId(userId.getValue());
    }

    public Review(ReviewComment reviewComment, @NonNull ReviewOverallClassification reviewOverallClassification,
                  List<ReviewAttributeClassification> reviewAttributeClassificationList
            ,@NonNull WifiSpotId wifiSpotId
            ,@NonNull UserId userId){
        this.reviewId = new ReviewId();
        this.reviewCreateDateTime = new ReviewCreateDateTime();
        this.reviewComment = (reviewComment != null ?  new ReviewComment(reviewComment.getValue()) : new ReviewComment());
        this.reviewOverallClassification =  new ReviewOverallClassification(reviewOverallClassification.getValue());
        this.reviewAttributeClassificationList = (reviewAttributeClassificationList !=null ? new ArrayList<>(reviewAttributeClassificationList) : new ArrayList<>());
        this.wifiSpotId = new WifiSpotId(wifiSpotId.getValue());
        this.userId = new UserId(userId.getValue());
    }
}
