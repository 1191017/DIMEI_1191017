package com.netquest.domain.review.dto;

import com.netquest.domain.user.dto.UserDto;
import com.netquest.domain.wifispot.dto.WifiSpotDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ReviewDetailedDto {
    UUID id;
    LocalDateTime reviewCreateDateTime;
    String reviewComment;
    int reviewOverallClassification;
    List<ReviewAttributeClassificationDto> reviewAttributeClassificationDtoList;
    WifiSpotDto wifiSpot;
    UserDto user;
}
