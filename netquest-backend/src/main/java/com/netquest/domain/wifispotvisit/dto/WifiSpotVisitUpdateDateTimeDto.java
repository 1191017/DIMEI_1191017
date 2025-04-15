package com.netquest.domain.wifispotvisit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WifiSpotVisitUpdateDateTimeDto {
    @NotNull
    private LocalDateTime dateTime;
}
