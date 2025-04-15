package com.netquest.domain.wifispotvisit.dto;

import com.netquest.domain.wifispot.dto.WifiSpotDto;

import java.time.LocalDateTime;
import java.util.UUID;


public record WifiSpotVisitHistoryDto(
        UUID id,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        UUID userId,
        WifiSpotDto wifiSpotDto
) {}
