package com.netquest.domain.wifispot.model;

import com.netquest.domain.shared.LocationType;
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
public class WifiSpotLocationType {
    @NotNull
    private final LocationType value;
    public WifiSpotLocationType(LocationType locationType){
        if (locationType == null) {
            throw new IllegalArgumentException("Location Type cannot be null");
        }
        value = locationType;
    }
}
