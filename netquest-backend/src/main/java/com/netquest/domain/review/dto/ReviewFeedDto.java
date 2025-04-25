package com.netquest.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
public class ReviewFeedDto {
    private UUID reviewId;
    private LocalDateTime reviewCreateDateTime;
    private String reviewComment;
    private int reviewOverallClassification;
    private List<ReviewAttributeClassificationDto> attributes;
    private UUID userId;
    private String username;
    private UUID wifiSpotId;
    private String wifiSpotName;
}