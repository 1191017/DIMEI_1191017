package com.netquest.domain.wifispotvisit.mapper.impl;

import com.netquest.domain.user.mapper.UserMapper;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.mapper.WifiSpotMapper;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitCreateDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitDetailedDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitDto;
import com.netquest.domain.wifispotvisit.dto.WifiSpotVisitUpdateDateTimeDto;
import com.netquest.domain.wifispotvisit.mapper.WifiSpotVisitMapper;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisit;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitEndDateTime;
import com.netquest.domain.wifispotvisit.model.WifiSpotVisitStartDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class WifiSpotVisitMapperImpl implements WifiSpotVisitMapper {

    private final WifiSpotMapper wifiSpotMapper;
    private final UserMapper userMapper;

    @Override
    public WifiSpotVisitDto toDto(WifiSpotVisit wifiSpotVisit) {

        if (wifiSpotVisit == null) {
            return null;
        }

        return new WifiSpotVisitDto(
                wifiSpotVisit.getWifiSpotVisitId() != null ? wifiSpotVisit.getWifiSpotVisitId().getValue() : null,
                wifiSpotVisit.getWifiSpotVisitStartDateTime() != null ? wifiSpotVisit.getWifiSpotVisitStartDateTime() .getValue() : null,
                wifiSpotVisit.getWifiSpotVisitEndDateTime() != null ? wifiSpotVisit.getWifiSpotVisitEndDateTime().getValue() : null,
                wifiSpotVisit.getUserId() != null ? wifiSpotVisit.getUserId().getValue() : null,
                wifiSpotVisit.getWifiSpotId() != null ? wifiSpotVisit.getWifiSpotId().getValue() : null);
    }

    @Override
    public WifiSpotVisitDetailedDto toDetailedDto(WifiSpotVisit wifiSpotVisit) {
        if (wifiSpotVisit == null) {
            return null;
        }

        return new WifiSpotVisitDetailedDto(
                wifiSpotVisit.getWifiSpotVisitId() != null ? wifiSpotVisit.getWifiSpotVisitId().getValue() : null,
                wifiSpotVisit.getWifiSpotVisitStartDateTime() != null ? wifiSpotVisit.getWifiSpotVisitStartDateTime() .getValue() : null,
                wifiSpotVisit.getWifiSpotVisitEndDateTime() != null ? wifiSpotVisit.getWifiSpotVisitEndDateTime().getValue() : null,
                userMapper.toUserDto(wifiSpotVisit.getUser()),
                wifiSpotMapper.wifiSpotDomainToDto(wifiSpotVisit.getWifiSpot()));
    }

    @Override
    public WifiSpotVisit toNewEntity(UUID userUUID, WifiSpotVisitCreateDto wifiSpotVisitCreateDto) {
        return new WifiSpotVisit(
                new WifiSpotVisitStartDateTime(wifiSpotVisitCreateDto.getStartDateTime()),
                new WifiSpotVisitEndDateTime(wifiSpotVisitCreateDto.getEndDateTime()),
                new WifiSpotId(wifiSpotVisitCreateDto.getWifiSpotId()),
                new UserId(userUUID));
    }

    @Override
    public WifiSpotVisitEndDateTime toEndDateTime(WifiSpotVisitUpdateDateTimeDto wifiSpotVisitUpdateDateTimeDto) {
        if (wifiSpotVisitUpdateDateTimeDto == null) {
            return null;
        }
        return new WifiSpotVisitEndDateTime(wifiSpotVisitUpdateDateTimeDto.getDateTime());
    }


}
