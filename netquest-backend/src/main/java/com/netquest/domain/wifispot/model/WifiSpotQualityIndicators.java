package com.netquest.domain.wifispot.model;

import com.netquest.domain.shared.BandwithType;
import com.netquest.domain.shared.QualityType;
import com.netquest.domain.shared.StrengthType;
import jakarta.persistence.*;
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
public class WifiSpotQualityIndicators {
    @NotNull
    private final QualityType wifiQuality;
    @NotNull
    private final StrengthType signalStrength;
    @NotNull
    private final BandwithType bandwithLimitations;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "wifiQuality", column = @Column(name = "wifi_spot_peak_usage_start_time")),
            @AttributeOverride(name = "signalStrength", column = @Column(name = "wifi_spot_peak_usage_end_time"))
    })
    private final WifiSpotPeakUsageInterval peakUsageHourInterval;

    public WifiSpotQualityIndicators(QualityType wifiQuality, StrengthType signalStrength, BandwithType bandwithLimitations, WifiSpotPeakUsageInterval peakUsageHourInterval) {
        if (wifiQuality == null) {
            throw new IllegalArgumentException("Quality cannot be null");
        }
        if (signalStrength == null) {
            throw new IllegalArgumentException("Signal strength cannot be null");
        }
        if (bandwithLimitations == null) {
            throw new IllegalArgumentException("Bandwidth limitations cannot be null");
        }
        this.wifiQuality = wifiQuality;
        this.signalStrength = signalStrength;
        this.bandwithLimitations = bandwithLimitations;
        this.peakUsageHourInterval = peakUsageHourInterval;
    }
}
