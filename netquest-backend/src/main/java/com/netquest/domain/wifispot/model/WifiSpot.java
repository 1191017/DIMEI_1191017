package com.netquest.domain.wifispot.model;

import com.netquest.domain.user.model.User;
import com.netquest.domain.user.model.UserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@Entity
@Table(name = "wifi_spot", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"wifi_spot_latitude", "wifi_spot_longitude", "wifi_spot_name"})
})

public class WifiSpot {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_id"))
    })
    private final WifiSpotId wifiSpotId;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_name", nullable = false))
    })
    private final WifiSpotName wifiSpotName;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_description", nullable = false))
    })
    private final WifiSpotDescription wifiSpotDescription;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "longitude", column = @Column(name = "wifi_spot_longitude", nullable = false)),
            @AttributeOverride(name = "latitude", column = @Column(name = "wifi_spot_latitude", nullable = false))
    })
    private final WifiSpotCoordinates wifiSpotCoordinates;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_location_type", nullable = false))
    })
    private final WifiSpotLocationType wifiSpotLocationType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "wifiQuality", column = @Column(name = "wifi_spot_wifi_quality", nullable = false)),
            @AttributeOverride(name = "signalStrength", column = @Column(name = "wifi_spot_signal_strength", nullable = false)),
            @AttributeOverride(name = "bandwithLimitations", column = @Column(name = "wifi_spot_bandwith_limitations", nullable = false))
    })
    private final WifiSpotQualityIndicators wifiSpotQualityIndicators;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "crowded", column = @Column(name = "wifi_spot_crowded", nullable = false)),
            @AttributeOverride(name = "coveredArea", column = @Column(name = "wifi_spot_covered_area", nullable = false)),
            @AttributeOverride(name = "airConditioning", column = @Column(name = "wifi_spot_air_conditioning", nullable = false)),
            @AttributeOverride(name = "outdoorSeating", column = @Column(name = "wifi_spot_outdoor_seating", nullable = false)),
            @AttributeOverride(name = "goodView", column = @Column(name = "wifi_spot_good_view", nullable = false)),
            @AttributeOverride(name = "noiseLevel", column = @Column(name = "wifi_spot_noise_level", nullable = false)),
            @AttributeOverride(name = "petFriendly", column = @Column(name = "wifi_spot_pet_friendly", nullable = false)),
            @AttributeOverride(name = "childFriendly", column = @Column(name = "wifi_spot_child_friendly", nullable = false)),
            @AttributeOverride(name = "disabledAccess", column = @Column(name = "wifi_spot_disabled_access", nullable = false))
    })
    private final WifiSpotEnvironmentalFeatures wifiSpotEnvironmentalFeatures;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "availablePowerOutlets", column = @Column(name = "wifi_spot_available_power_outlets", nullable = false)),
            @AttributeOverride(name = "chargingStations", column = @Column(name = "wifi_spot_charging_stations", nullable = false)),
            @AttributeOverride(name = "restroomsAvailable", column = @Column(name = "wifi_spot_restrooms_available", nullable = false)),
            @AttributeOverride(name = "parkingAvailability", column = @Column(name = "wifi_spot_parking_availability", nullable = false)),
            @AttributeOverride(name = "foodOptions", column = @Column(name = "wifi_spot_food_options", nullable = false)),
            @AttributeOverride(name = "drinkOptions", column = @Column(name = "wifi_spot_drink_options", nullable = false))
    })
    private final WifiSpotFacilities wifiSpotFacilities;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "openDuringRain", column = @Column(name = "wifi_spot_open_during_rain")),
            @AttributeOverride(name = "openDuringHeat", column = @Column(name = "wifi_spot_open_during_heat")),
            @AttributeOverride(name = "heatedInWinter", column = @Column(name = "wifi_spot_heated_in_winter")),
            @AttributeOverride(name = "shadedAreas", column = @Column(name = "wifi_spot_shaded_areas")),
            @AttributeOverride(name = "outdoorFans", column = @Column(name = "wifi_spot_outdoor_fans"))
    })
    private final WifiSpotWeatherFeatures wifiSpotWeatherFeatures;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wifi_spot_address_id", referencedColumnName = "wifi_spot_address_id", nullable = false)
    private final WifiSpotAddress wifiSpotAddress;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_management", nullable = false))
    })
    private final WifiSpotManagement wifiSpotManagement;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_create_date_time", nullable = true))
    })
    private final WifiSpotCreateDateTime wifiSpotCreateDateTime;

    @Embedded
    @NotNull
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "wifi_spot_user_id"))
    })
    private UserId userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wifi_spot_user_id", insertable = false, updatable = false)
    private User user;

    public WifiSpot(
            WifiSpotName wifiSpotName
            , WifiSpotDescription wifiSpotDescription
            , WifiSpotCoordinates wifiSpotCoordinates
            , WifiSpotLocationType wifiSpotLocationType
            , WifiSpotQualityIndicators wifiSpotQualityIndicators
            , WifiSpotEnvironmentalFeatures wifiSpotEnvironmentalFeatures
            , WifiSpotFacilities wifiSpotFacilities
            , WifiSpotWeatherFeatures wifiSpotWeatherFeatures
            , WifiSpotAddress wifiSpotAddress
            , WifiSpotManagement wifiSpotManagement
            , UserId userId
    ) {
        this.wifiSpotId = new WifiSpotId();
        this.wifiSpotName = wifiSpotName;
        this.wifiSpotDescription = wifiSpotDescription;
        this.wifiSpotCoordinates = wifiSpotCoordinates;
        this.wifiSpotLocationType = wifiSpotLocationType;
        this.wifiSpotQualityIndicators = wifiSpotQualityIndicators;
        this.wifiSpotEnvironmentalFeatures = wifiSpotEnvironmentalFeatures;
        this.wifiSpotFacilities = wifiSpotFacilities;
        this.wifiSpotWeatherFeatures = wifiSpotWeatherFeatures;
        this.wifiSpotAddress = wifiSpotAddress;
        this.wifiSpotManagement = wifiSpotManagement;
        this.userId = userId;
        this.wifiSpotCreateDateTime = new WifiSpotCreateDateTime();
    }

    public WifiSpot(
            WifiSpotId wifiSpotId
            , WifiSpotName wifiSpotName
            , WifiSpotDescription wifiSpotDescription
            , WifiSpotCoordinates wifiSpotCoordinates
            , WifiSpotLocationType wifiSpotLocationType
            , WifiSpotQualityIndicators wifiSpotQualityIndicators
            , WifiSpotEnvironmentalFeatures wifiSpotEnvironmentalFeatures
            , WifiSpotFacilities wifiSpotFacilities
            , WifiSpotWeatherFeatures wifiSpotWeatherFeatures
            , WifiSpotAddress wifiSpotAddress
            , WifiSpotManagement wifiSpotManagement
            , UserId userId
    ) {
        this.wifiSpotId = wifiSpotId;
        this.wifiSpotName = wifiSpotName;
        this.wifiSpotDescription = wifiSpotDescription;
        this.wifiSpotCoordinates = wifiSpotCoordinates;
        this.wifiSpotLocationType = wifiSpotLocationType;
        this.wifiSpotQualityIndicators = wifiSpotQualityIndicators;
        this.wifiSpotEnvironmentalFeatures = wifiSpotEnvironmentalFeatures;
        this.wifiSpotFacilities = wifiSpotFacilities;
        this.wifiSpotWeatherFeatures = wifiSpotWeatherFeatures;
        this.wifiSpotAddress = wifiSpotAddress;
        this.wifiSpotManagement = wifiSpotManagement;
        this.userId = userId;
        this.wifiSpotCreateDateTime = new WifiSpotCreateDateTime();
    }

    public WifiSpot update(
        WifiSpotName wifiSpotName,
        WifiSpotDescription wifiSpotDescription,
        WifiSpotCoordinates wifiSpotCoordinates,
        WifiSpotLocationType wifiSpotLocationType,
        WifiSpotQualityIndicators wifiSpotQualityIndicators,
        WifiSpotEnvironmentalFeatures wifiSpotEnvironmentalFeatures,
        WifiSpotFacilities wifiSpotFacilities,
        WifiSpotWeatherFeatures wifiSpotWeatherFeatures,
        WifiSpotAddress wifiSpotAddress,
        WifiSpotManagement wifiSpotManagement
    ) {
        return new WifiSpot(
            this.wifiSpotId, // Retain the existing ID
            wifiSpotName != null ? wifiSpotName : this.wifiSpotName,
            wifiSpotDescription != null ? wifiSpotDescription : this.wifiSpotDescription,
            wifiSpotCoordinates != null ? wifiSpotCoordinates : this.wifiSpotCoordinates,
            wifiSpotLocationType != null ? wifiSpotLocationType : this.wifiSpotLocationType,
            wifiSpotQualityIndicators != null ? wifiSpotQualityIndicators : this.wifiSpotQualityIndicators,
            wifiSpotEnvironmentalFeatures != null ? wifiSpotEnvironmentalFeatures : this.wifiSpotEnvironmentalFeatures,
            wifiSpotFacilities != null ? wifiSpotFacilities : this.wifiSpotFacilities,
            wifiSpotWeatherFeatures != null ? wifiSpotWeatherFeatures : this.wifiSpotWeatherFeatures,
            wifiSpotAddress != null ? wifiSpotAddress : this.wifiSpotAddress,
            wifiSpotManagement != null ? wifiSpotManagement : this.wifiSpotManagement,
            this.userId // Retain the existing user ID
        );
    }

}
