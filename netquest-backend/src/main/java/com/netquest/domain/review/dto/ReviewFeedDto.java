package com.netquest.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
public class ReviewFeedDto {
    private UUID reviewId;
    private LocalDateTime reviewCreateDateTime;
    private String reviewComment;
    private int reviewOverallClassification;
    private UUID userId;
    private String username;
    private String userEmail;
    private UUID wifiSpotId;
    private String wifiSpotName;
    private String wifiSpotDescription;
}