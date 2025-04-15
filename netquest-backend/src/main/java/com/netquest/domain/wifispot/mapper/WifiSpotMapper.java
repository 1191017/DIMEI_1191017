package com.netquest.domain.wifispot.mapper;

import com.netquest.domain.user.model.User;
import com.netquest.domain.wifispot.dto.WifiSpotAddressCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotAddressDto;
import com.netquest.domain.wifispot.dto.WifiSpotCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotDto;
import com.netquest.domain.wifispot.model.WifiSpot;
import com.netquest.domain.wifispot.model.WifiSpotAddress;

import java.util.UUID;

public interface WifiSpotMapper {

    WifiSpotDto wifiSpotDomainToDto(WifiSpot wifiSpot);

    WifiSpot wifiSpotCreateDtoToDomain(WifiSpotCreateDto wifiSpotDto, UUID userUUID);
    WifiSpot wifiSpotDtoToDomain(WifiSpotDto wifiSpotDto);
    WifiSpotAddressDto wifiSpotAddressDomainToDto(WifiSpotAddress wifiSpotAddress);

    WifiSpotAddress wifiSpotAddressDtoToDomain(WifiSpotAddressDto wifiSpotAddressDto);

    WifiSpotAddressCreateDto wifiSpotAddressDomainToCreateDto(WifiSpotAddress wifiSpotAddress);

    WifiSpotAddress wifiSpotAddressCreateDtoToDomain(WifiSpotAddressCreateDto wifiSpotAddressDto);
}