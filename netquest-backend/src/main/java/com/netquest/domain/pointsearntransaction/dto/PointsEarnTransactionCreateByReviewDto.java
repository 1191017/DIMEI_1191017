package com.netquest.domain.pointsearntransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PointsEarnTransactionCreateByReviewDto {
    UUID userUUID;
    UUID reviewUUID;
    UUID wifiSpotUUID;
}
