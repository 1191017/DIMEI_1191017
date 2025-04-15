package com.netquest.domain.wifispot.dto;

import java.util.UUID;

public record WifiSpotAddressDto(UUID uuid, String country, String zipCode, String addressLine1, String addressLine2,
                                 String city, String district) {

}
