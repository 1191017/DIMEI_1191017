package com.netquest.domain.pointsearntransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class PointsEarnTransactionDto{
        UUID id;
        LocalDateTime datetime;
        long amount;
        UUID userId;
        UUID wifiSpotId;
        UUID wifiSpotVisitId;
}
