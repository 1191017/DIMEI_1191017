package com.netquest.domain.pointsearntransaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PointsEarnTransactionCreateByWifiSpotCreationDto {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID wifiSpotId;


}
