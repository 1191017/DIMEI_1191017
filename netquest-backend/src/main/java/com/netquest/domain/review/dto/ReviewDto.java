package com.netquest.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
public class ReviewDto {
    UUID reviewId;
    LocalDateTime reviewCreateDateTime;
    String reviewComment;
    int reviewOverallClassification;
    List<ReviewAttributeClassificationDto> reviewAttributeClassificationDtoList;
    UUID wifiSpotId;
    UUID userId;
    String username;
}
