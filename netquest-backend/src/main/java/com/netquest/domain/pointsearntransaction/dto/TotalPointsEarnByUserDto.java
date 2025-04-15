package com.netquest.domain.pointsearntransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TotalPointsEarnByUserDto {
    UUID userId;
    long points;
}
