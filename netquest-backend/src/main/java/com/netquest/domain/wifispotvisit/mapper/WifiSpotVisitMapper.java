package com.netquest.domain.wifispotvisit.mapper;

import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitCreateDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitDetailedDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitUpdateDateTimeDto;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisit;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitEndDateTime;

import java.util.UUID;

public interface WifiSpotVisitMapper {
    WifiSpotVisitDto toDto(WifiSpotVisit wifiSpotVisit);
    WifiSpotVisit toNewEntity(UUID userUUID, WifiSpotVisitCreateDto wifiSpotVisitCreateDto);
    WifiSpotVisitEndDateTime toEndDateTime(WifiSpotVisitUpdateDateTimeDto wifiSpotVisitUpdateDateTimeDto);

    WifiSpotVisitDetailedDto toDetailedDto(WifiSpotVisit wifiSpotVisit);
}
