package com.netquest.domain.wifispotvisit.dto;

import java.time.LocalDateTime;
import java.util.UUID;


public record WifiSpotVisitDto (
        UUID id,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        UUID userId,
        UUID wifiSpotId
) {}
