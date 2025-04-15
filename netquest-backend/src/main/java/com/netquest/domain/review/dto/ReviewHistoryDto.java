package com.netquest.domain.review.dto;

import com.netquest.domain.wifispot.dto.WifiSpotDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
public class ReviewHistoryDto {
    UUID reviewId;
    LocalDateTime reviewCreateDateTime;
    String reviewComment;
    int reviewOverallClassification;
    List<ReviewAttributeClassificationDto> reviewAttributeClassificationDtoList;
    WifiSpotDto wifiSpotDto;
    UUID userId;
}
