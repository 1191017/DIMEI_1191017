package com.netquest.domain.wifispot.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true)
public class WifiSpotFacilities {
    @NotNull
    private final boolean availablePowerOutlets;
    @NotNull
    private final boolean chargingStations;
    @NotNull
    private final boolean restroomsAvailable;
    @NotNull
    private final boolean parkingAvailability;
    @NotNull
    private final boolean foodOptions;
    @NotNull
    private final boolean drinkOptions;

    public WifiSpotFacilities(boolean availablePowerOutlets, boolean chargingStations, boolean restroomsAvailable, boolean parkingAvailability, boolean foodOptions, boolean drinkOptions) {
        this.availablePowerOutlets = availablePowerOutlets;
        this.chargingStations = chargingStations;
        this.restroomsAvailable = restroomsAvailable;
        this.parkingAvailability = parkingAvailability;
        this.foodOptions = foodOptions;
        this.drinkOptions = drinkOptions;
    }
}
