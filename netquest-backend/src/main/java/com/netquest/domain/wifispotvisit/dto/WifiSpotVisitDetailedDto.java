package com.netquest.domain.wifispotvisit.dto;

import com.netquest.domain.user.dto.UserDto;
import com.netquest.domain.wifispot.dto.WifiSpotDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WifiSpotVisitDetailedDto {
    UUID id;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
    UserDto user;
    WifiSpotDto wifiSpot;
}
