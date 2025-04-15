package com.netquest.domain.wifispot.dto;

public record WifiSpotAddressCreateDto(String country, String zipCode, String addressLine1, String addressLine2,
                                       String city, String district) {

}
