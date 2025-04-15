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
public class WifiSpotCoordinates {
    @NotNull
    private final double longitude;
    @NotNull
    private final double latitude;
    public WifiSpotCoordinates(double longitude, double latitude){
        if (Double.isNaN(longitude) || Double.isNaN(latitude)) {
            throw new IllegalArgumentException("Coordinates cannot be NaN");
        }
        /*if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }*/
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
