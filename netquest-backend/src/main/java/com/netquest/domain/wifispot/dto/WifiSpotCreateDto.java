package com.netquest.domain.wifispot.dto;

import com.netquest.domain.shared.*;

import java.time.LocalTime;

public record WifiSpotCreateDto(
        String name,
        String description,
        double latitude,
        double longitude,
        LocationType locationType,
        QualityType wifiQuality,
        StrengthType signalStrength,
        BandwithType bandwidth,
        LocalTime peakUsageStart,
        LocalTime peakUsageEnd,
        boolean crowded,
        boolean coveredArea,
        boolean airConditioning,
        boolean outdoorSeating,
        boolean goodView,
        NoiseType noiseLevel,
        boolean petFriendly,
        boolean childFriendly,
        boolean disableAccess,
        boolean availablePowerOutlets,
        boolean chargingStations,
        boolean restroomsAvailable,
        boolean parkingAvailability,
        boolean foodOptions,
        boolean drinkOptions,
        boolean openDuringRain,
        boolean openDuringHeat,
        boolean heatedInWinter,
        boolean shadedAreas,
        boolean outdoorFans,
        WifiSpotAddressCreateDto address,
        WifiSpotManagementType wifiSpotManagementType
) {
}