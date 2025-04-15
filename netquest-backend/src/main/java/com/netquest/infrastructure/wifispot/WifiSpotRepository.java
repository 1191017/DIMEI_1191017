package com.netquest.infrastructure.wifispot;

import com.netquest.domain.shared.*;
import com.netquest.domain.user.model.User;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.model.WifiSpot;
import com.netquest.domain.wifispot.model.WifiSpotId;
import com.netquest.domain.wifispot.model.WifiSpotName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WifiSpotRepository extends JpaRepository<WifiSpot, WifiSpotId> {
    @Query("SELECT COUNT(wfs) FROM WifiSpot wfs")
    int getNumberOfWifiSpots();

    WifiSpot getWifiSpotByWifiSpotId(WifiSpotId wifiSpotId);


    List<WifiSpot> findByWifiSpotNameIn(List<WifiSpotName> wifiSpotIds);

    @Query("SELECT wfs FROM WifiSpot wfs " +
            "WHERE ( wfs.wifiSpotName.value like :name  OR :name is null) " +
            "AND (wfs.wifiSpotDescription.value like :description OR :description is null) " +
            "AND (wfs.wifiSpotLocationType.value = :locationType OR :locationType is null) " +
            "AND (wfs.wifiSpotQualityIndicators.wifiQuality = :wifiQuality OR :wifiQuality is null) " +
            "AND (wfs.wifiSpotQualityIndicators.signalStrength = :signalStrength or :signalStrength is null)" +
            "AND (wfs.wifiSpotQualityIndicators.bandwithLimitations = :bandwidth or :bandwidth is null)" +
            "AND (wfs.wifiSpotEnvironmentalFeatures.crowded = :crowded or :crowded is null)" +
            "AND (wfs.wifiSpotEnvironmentalFeatures.coveredArea = :coveredArea or :coveredArea is null)" +
            "AND (wfs.wifiSpotEnvironmentalFeatures.airConditioning = :airConditioning or :airConditioning is null)" +
            "AND (wfs.wifiSpotEnvironmentalFeatures.goodView = :goodView or :goodView is null)" +
            "AND (wfs.wifiSpotEnvironmentalFeatures.noiseLevel = :noiseLevel or :noiseLevel is null)" +
            "AND (wfs.wifiSpotEnvironmentalFeatures.petFriendly = :petFriendly or :petFriendly is null)" +
            "AND (wfs.wifiSpotEnvironmentalFeatures.childFriendly = :childFriendly or :childFriendly is null)" +
            "AND (wfs.wifiSpotEnvironmentalFeatures.disabledAccess = :disableAccess or :disableAccess is null)" +
            "AND (wfs.wifiSpotFacilities.availablePowerOutlets = :availablePowerOutlets or :availablePowerOutlets is null)" +
            "AND (wfs.wifiSpotFacilities.chargingStations = :chargingStations or :chargingStations is null)" +
            "AND (wfs.wifiSpotFacilities.restroomsAvailable = :restroomsAvailable or :restroomsAvailable is null)" +
            "AND (wfs.wifiSpotFacilities.parkingAvailability = :parkingAvailability or :parkingAvailability is null)" +
            "AND (wfs.wifiSpotFacilities.foodOptions = :foodOptions or :foodOptions is null)" +
            "AND (wfs.wifiSpotFacilities.drinkOptions = :drinkOptions or :drinkOptions is null)" +
            "AND (wfs.wifiSpotWeatherFeatures.openDuringRain = :openDuringRain or :openDuringRain is null)" +
            "AND (wfs.wifiSpotWeatherFeatures.openDuringHeat = :openDuringHeat or :openDuringHeat is null)" +
            "AND (wfs.wifiSpotWeatherFeatures.heatedInWinter = :heatedInWinter or :heatedInWinter is null)" +
            "AND (wfs.wifiSpotWeatherFeatures.shadedAreas = :shadedAreas or :shadedAreas is null)" +
            "AND (wfs.wifiSpotWeatherFeatures.outdoorFans = :outdoorFans or :outdoorFans is null)")
    List<WifiSpot> findFilteredWifiSpots(
        @Param("name") String name,
        @Param("description") String description,
        @Param("locationType") LocationType locationType,
        @Param("wifiQuality") QualityType wifiQuality,
        @Param("signalStrength") StrengthType signalStrength,
        @Param("bandwidth") BandwithType bandwidth,
        @Param("crowded") Boolean crowded,
        @Param("coveredArea") Boolean coveredArea,
        @Param("airConditioning") Boolean airConditioning,
        @Param("goodView") Boolean goodView,
        @Param("noiseLevel") NoiseType noiseLevel,
        @Param("petFriendly") Boolean petFriendly,
        @Param("childFriendly") Boolean childFriendly,
        @Param("disableAccess") Boolean disableAccess,
        @Param("availablePowerOutlets") Boolean availablePowerOutlets,
        @Param("chargingStations") Boolean chargingStations,
        @Param("restroomsAvailable") Boolean restroomsAvailable,
        @Param("parkingAvailability") Boolean parkingAvailability,
        @Param("foodOptions") Boolean foodOptions,
        @Param("drinkOptions") Boolean drinkOptions,
        @Param("openDuringRain") Boolean openDuringRain,
        @Param("openDuringHeat") Boolean openDuringHeat,
        @Param("heatedInWinter") Boolean heatedInWinter,
        @Param("shadedAreas") Boolean shadedAreas,
        @Param("outdoorFans") Boolean outdoorFans
    );


    List<WifiSpot> findByUserId(UserId userId);
}
