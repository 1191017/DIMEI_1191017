package com.netquest.domain.wifispot.dto;

import com.netquest.domain.shared.*;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class WifiSpotFilterDto {
    String name;
    Boolean exactName;
    String description;
    Boolean exactDescription;
    LocationType locationType;
    QualityType wifiQuality;
    StrengthType signalStrength;
    BandwithType bandwidth;
    Boolean crowded;
    Boolean coveredArea;
    Boolean airConditioning;
    Boolean goodView;
    NoiseType noiseLevel;
    Boolean petFriendly;
    Boolean childFriendly;
    Boolean disableAccess;
    Boolean availablePowerOutlets;
    Boolean chargingStations;
    Boolean restroomsAvailable;
    Boolean parkingAvailability;
    Boolean foodOptions;
    Boolean drinkOptions;
    Boolean openDuringRain;
    Boolean openDuringHeat;
    Boolean heatedInWinter;
    Boolean shadedAreas;
    Boolean outdoorFans;
}
