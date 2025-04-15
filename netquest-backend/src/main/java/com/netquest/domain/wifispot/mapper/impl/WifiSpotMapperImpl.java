package com.netquest.domain.wifispot.mapper.impl;

import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.dto.WifiSpotAddressCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotAddressDto;
import com.netquest.domain.wifispot.dto.WifiSpotCreateDto;
import com.netquest.domain.wifispot.dto.WifiSpotDto;
import com.netquest.domain.wifispot.mapper.WifiSpotMapper;
import com.netquest.domain.wifispot.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WifiSpotMapperImpl implements WifiSpotMapper {
    @Override
    public WifiSpotDto wifiSpotDomainToDto(WifiSpot wifiSpot) {

        if (wifiSpot == null) {
            return null;
        }

        UUID userUUID = (wifiSpot.getUserId() != null) ? wifiSpot.getUserId().getValue() : null;
        LocalDateTime wifiSpotCreateDateTime = (wifiSpot.getWifiSpotCreateDateTime() != null) ? wifiSpot.getWifiSpotCreateDateTime().getValue() : null;
        return new WifiSpotDto(
                wifiSpot.getWifiSpotId().getValue(),
                userUUID,
                wifiSpot.getWifiSpotName().getValue(),
                wifiSpot.getWifiSpotDescription().getValue(),
                wifiSpot.getWifiSpotCoordinates().getLatitude(),
                wifiSpot.getWifiSpotCoordinates().getLongitude(),
                wifiSpot.getWifiSpotLocationType().getValue(),
                wifiSpot.getWifiSpotQualityIndicators().getWifiQuality(),
                wifiSpot.getWifiSpotQualityIndicators().getSignalStrength(),
                wifiSpot.getWifiSpotQualityIndicators().getBandwithLimitations(),
                wifiSpot.getWifiSpotQualityIndicators().getPeakUsageHourInterval() != null ? wifiSpot.getWifiSpotQualityIndicators().getPeakUsageHourInterval().getStartTime() : null,
                wifiSpot.getWifiSpotQualityIndicators().getPeakUsageHourInterval() != null ? wifiSpot.getWifiSpotQualityIndicators().getPeakUsageHourInterval().getEndTime() : null,
                wifiSpot.getWifiSpotEnvironmentalFeatures().isCrowded(),
                wifiSpot.getWifiSpotEnvironmentalFeatures().isCoveredArea(),
                wifiSpot.getWifiSpotEnvironmentalFeatures().isAirConditioning(),
                wifiSpot.getWifiSpotEnvironmentalFeatures().isOutdoorSeating(),
                wifiSpot.getWifiSpotEnvironmentalFeatures().isGoodView(),
                wifiSpot.getWifiSpotEnvironmentalFeatures().getNoiseLevel(),
                wifiSpot.getWifiSpotEnvironmentalFeatures().isPetFriendly(),
                wifiSpot.getWifiSpotEnvironmentalFeatures().isChildFriendly(),
                wifiSpot.getWifiSpotEnvironmentalFeatures().isDisabledAccess(),
                wifiSpot.getWifiSpotFacilities().isAvailablePowerOutlets(),
                wifiSpot.getWifiSpotFacilities().isChargingStations(),
                wifiSpot.getWifiSpotFacilities().isRestroomsAvailable(),
                wifiSpot.getWifiSpotFacilities().isParkingAvailability(),
                wifiSpot.getWifiSpotFacilities().isFoodOptions(),
                wifiSpot.getWifiSpotFacilities().isDrinkOptions(),
                wifiSpot.getWifiSpotWeatherFeatures().isOpenDuringRain(),
                wifiSpot.getWifiSpotWeatherFeatures().isOpenDuringHeat(),
                wifiSpot.getWifiSpotWeatherFeatures().isHeatedInWinter(),
                wifiSpot.getWifiSpotWeatherFeatures().isShadedAreas(),
                wifiSpot.getWifiSpotWeatherFeatures().isOutdoorFans(),
                wifiSpotAddressDomainToDto(wifiSpot.getWifiSpotAddress()),
                wifiSpot.getWifiSpotManagement().getValue(),
                wifiSpotCreateDateTime
        );
    }

    @Override
    public WifiSpot wifiSpotCreateDtoToDomain(WifiSpotCreateDto wifiSpotDto, UUID userUUID) {
        return new WifiSpot(
                new WifiSpotName(wifiSpotDto.name()),
                new WifiSpotDescription(wifiSpotDto.description()),
                new WifiSpotCoordinates(wifiSpotDto.longitude(), wifiSpotDto.latitude()),
                new WifiSpotLocationType(wifiSpotDto.locationType()),
                new WifiSpotQualityIndicators(
                        wifiSpotDto.wifiQuality(),
                        wifiSpotDto.signalStrength(),
                        wifiSpotDto.bandwidth(),
                        new WifiSpotPeakUsageInterval(wifiSpotDto.peakUsageStart(), wifiSpotDto.peakUsageEnd())
                ),
                new WifiSpotEnvironmentalFeatures(
                        wifiSpotDto.crowded(),
                        wifiSpotDto.coveredArea(),
                        wifiSpotDto.airConditioning(),
                        wifiSpotDto.outdoorSeating(),
                        wifiSpotDto.goodView(),
                        wifiSpotDto.noiseLevel(),
                        wifiSpotDto.petFriendly(),
                        wifiSpotDto.childFriendly(),
                        wifiSpotDto.disableAccess()
                ),
                new WifiSpotFacilities(
                        wifiSpotDto.availablePowerOutlets(),
                        wifiSpotDto.chargingStations(),
                        wifiSpotDto.restroomsAvailable(),
                        wifiSpotDto.parkingAvailability(),
                        wifiSpotDto.foodOptions(),
                        wifiSpotDto.drinkOptions()
                ),
                new WifiSpotWeatherFeatures(
                        wifiSpotDto.openDuringRain(),
                        wifiSpotDto.openDuringHeat(),
                        wifiSpotDto.heatedInWinter(),
                        wifiSpotDto.shadedAreas(),
                        wifiSpotDto.outdoorFans()
                ),
                wifiSpotAddressCreateDtoToDomain(wifiSpotDto.address()),
                new WifiSpotManagement(
                        wifiSpotDto.wifiSpotManagementType()
                ),
                new UserId(userUUID)
        );
    }

    @Override
    public WifiSpot wifiSpotDtoToDomain(WifiSpotDto wifiSpotDto) {
        return new WifiSpot(
                new WifiSpotId(wifiSpotDto.uuid()),
                new WifiSpotName(wifiSpotDto.name()),
                new WifiSpotDescription(wifiSpotDto.description()),
                new WifiSpotCoordinates(wifiSpotDto.longitude(), wifiSpotDto.latitude()),
                new WifiSpotLocationType(wifiSpotDto.locationType()),
                new WifiSpotQualityIndicators(
                        wifiSpotDto.wifiQuality(),
                        wifiSpotDto.signalStrength(),
                        wifiSpotDto.bandwidth(),
                        new WifiSpotPeakUsageInterval(wifiSpotDto.peakUsageStart(), wifiSpotDto.peakUsageEnd())
                ),
                new WifiSpotEnvironmentalFeatures(
                        wifiSpotDto.crowded(),
                        wifiSpotDto.coveredArea(),
                        wifiSpotDto.airConditioning(),
                        wifiSpotDto.outdoorSeating(),
                        wifiSpotDto.goodView(),
                        wifiSpotDto.noiseLevel(),
                        wifiSpotDto.petFriendly(),
                        wifiSpotDto.childFriendly(),
                        wifiSpotDto.disableAccess()
                ),
                new WifiSpotFacilities(
                        wifiSpotDto.availablePowerOutlets(),
                        wifiSpotDto.chargingStations(),
                        wifiSpotDto.restroomsAvailable(),
                        wifiSpotDto.parkingAvailability(),
                        wifiSpotDto.foodOptions(),
                        wifiSpotDto.drinkOptions()
                ),
                new WifiSpotWeatherFeatures(
                        wifiSpotDto.openDuringRain(),
                        wifiSpotDto.openDuringHeat(),
                        wifiSpotDto.heatedInWinter(),
                        wifiSpotDto.shadedAreas(),
                        wifiSpotDto.outdoorFans()
                ),
                wifiSpotAddressDtoToDomain(wifiSpotDto.address()),
                new WifiSpotManagement(
                        wifiSpotDto.wifiSpotManagementType()
                ),
                new UserId(wifiSpotDto.userId())
        );
    }

    @Override
    public WifiSpotAddressDto wifiSpotAddressDomainToDto(WifiSpotAddress wifiSpotAddress) {
        return new WifiSpotAddressDto(
                wifiSpotAddress.getWifiSpotAddressId().getValue(),
                wifiSpotAddress.getWifiSpotAddressCountry().getValue(),
                wifiSpotAddress.getWifiSpotAddressZipCode().getValue(),
                wifiSpotAddress.getWifiSpotAddressLine1().getValue(),
                wifiSpotAddress.getWifiSpotAddressLine2() != null ? wifiSpotAddress.getWifiSpotAddressLine2().getValue() : null,
                wifiSpotAddress.getWifiSpotAddressCity().getValue(),
                wifiSpotAddress.getWifiSpotAddressDistrict().getValue()
        );
    }

    @Override
    public WifiSpotAddress wifiSpotAddressDtoToDomain(WifiSpotAddressDto wifiSpotAddressDto) {
        return new WifiSpotAddress(
                new WifiSpotAddressId(wifiSpotAddressDto.uuid()),
                new WifiSpotAddressCountry(wifiSpotAddressDto.country()),
                new WifiSpotAddressZipCode(wifiSpotAddressDto.zipCode()),
                new WifiSpotAddressLine1(wifiSpotAddressDto.addressLine1()),
                new WifiSpotAddressLine2(wifiSpotAddressDto.addressLine2()),
                new WifiSpotAddressCity(wifiSpotAddressDto.city()),
                new WifiSpotAddressDistrict(wifiSpotAddressDto.district())
        );
    }

    @Override
    public WifiSpotAddressCreateDto wifiSpotAddressDomainToCreateDto(WifiSpotAddress wifiSpotAddress) {
        return new WifiSpotAddressCreateDto(
                wifiSpotAddress.getWifiSpotAddressCountry().getValue(),
                wifiSpotAddress.getWifiSpotAddressZipCode().getValue(),
                wifiSpotAddress.getWifiSpotAddressLine1().getValue(),
                wifiSpotAddress.getWifiSpotAddressLine2() != null ? wifiSpotAddress.getWifiSpotAddressLine2().getValue() : null,
                wifiSpotAddress.getWifiSpotAddressCity().getValue(),
                wifiSpotAddress.getWifiSpotAddressDistrict().getValue()
        );
    }

    @Override
    public WifiSpotAddress wifiSpotAddressCreateDtoToDomain(WifiSpotAddressCreateDto wifiSpotAddressDto) {
        return new WifiSpotAddress(
                new WifiSpotAddressCountry(wifiSpotAddressDto.country()),
                new WifiSpotAddressZipCode(wifiSpotAddressDto.zipCode()),
                new WifiSpotAddressLine1(wifiSpotAddressDto.addressLine1()),
                new WifiSpotAddressLine2(wifiSpotAddressDto.addressLine2()),
                new WifiSpotAddressCity(wifiSpotAddressDto.city()),
                new WifiSpotAddressDistrict(wifiSpotAddressDto.district())
        );
    }
}
