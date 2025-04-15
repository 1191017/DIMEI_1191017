package com.netquest.domain.wifispot.model;

import com.netquest.domain.shared.NoiseType;
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
public class WifiSpotEnvironmentalFeatures {
    @NotNull
    private final boolean crowded;
    @NotNull
    private final boolean coveredArea;
    @NotNull
    private final boolean airConditioning;
    @NotNull
    private final boolean outdoorSeating;
    @NotNull
    private final boolean goodView;
    @NotNull
    private final NoiseType noiseLevel;
    @NotNull
    private final boolean petFriendly;
    @NotNull
    private final boolean childFriendly;
    @NotNull
    private final boolean disabledAccess;

    public WifiSpotEnvironmentalFeatures(boolean crowded, boolean coveredArea, boolean airConditioning, boolean outdoorSeating, boolean goodView, NoiseType noiseLevel, boolean petFriendly, boolean childFriendly, boolean disabledAccess) {
        if (noiseLevel == null) {
            throw new IllegalArgumentException("Noise level cannot be null");
        }
        this.crowded = crowded;
        this.coveredArea = coveredArea;
        this.airConditioning = airConditioning;
        this.outdoorSeating = outdoorSeating;
        this.goodView = goodView;
        this.noiseLevel = noiseLevel;
        this.petFriendly = petFriendly;
        this.childFriendly = childFriendly;
        this.disabledAccess = disabledAccess;
    }
}
