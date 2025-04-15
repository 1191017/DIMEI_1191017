package com.netquest.domain.wifispot.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor(force = true)
public class WifiSpotWeatherFeatures {
    private final boolean openDuringRain;
    private final boolean openDuringHeat;
    private final boolean heatedInWinter;
    private final boolean shadedAreas;
    private final boolean outdoorFans;

    public WifiSpotWeatherFeatures(boolean openDuringRain, boolean openDuringHeat, boolean heatedInWinter, boolean shadedAreas, boolean outdoorFans) {
        this.openDuringRain = openDuringRain;
        this.openDuringHeat = openDuringHeat;
        this.heatedInWinter = heatedInWinter;
        this.shadedAreas = shadedAreas;
        this.outdoorFans = outdoorFans;
    }
}
