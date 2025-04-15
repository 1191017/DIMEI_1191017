package com.netquest.domain.pointsearntransaction.dto;

import com.netquest.domain.review.dto.ReviewDetailedDto;
import com.netquest.domain.user.dto.UserDto;
import com.netquest.domain.wifispot.dto.WifiSpotDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitDetailedDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointsEarnTransactionDetailedDto {
    UUID id;
    LocalDateTime dateTime;
    long amount;
    UserDto user;
    WifiSpotDto wifiSpot;
    WifiSpotVisitDetailedDto wifiSpotVisit;
    WifiSpotVisitDetailedDto wifiSpotVisitByOther;
    ReviewDetailedDto review;
}
