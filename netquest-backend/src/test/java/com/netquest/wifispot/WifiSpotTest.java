package com.netquest.wifispot;

import com.netquest.domain.shared.*;
import com.netquest.domain.user.model.UserId;
import com.netquest.domain.wifispot.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WifiSpotTest {

    private WifiSpotName wifiSpotName;
    private WifiSpotDescription wifiSpotDescription;
    private WifiSpotCoordinates wifiSpotCoordinates;
    private WifiSpotLocationType wifiSpotLocationType;
    private WifiSpotQualityIndicators wifiSpotQualityIndicators;
    private WifiSpotEnvironmentalFeatures wifiSpotEnvironmentalFeatures;
    private WifiSpotFacilities wifiSpotFacilities;
    private WifiSpotWeatherFeatures wifiSpotWeatherFeatures;
    private WifiSpotAddress wifiSpotAddress;
    private WifiSpotManagement wifiSpotManagement;
    private UserId userId;

    @BeforeEach
    void setUp() {
        wifiSpotName = mock(WifiSpotName.class);
        wifiSpotDescription = mock(WifiSpotDescription.class);
        wifiSpotCoordinates = mock(WifiSpotCoordinates.class);
        wifiSpotLocationType = mock(WifiSpotLocationType.class);
        wifiSpotQualityIndicators = mock(WifiSpotQualityIndicators.class);
        wifiSpotEnvironmentalFeatures = mock(WifiSpotEnvironmentalFeatures.class);
        wifiSpotFacilities = mock(WifiSpotFacilities.class);
        wifiSpotWeatherFeatures = mock(WifiSpotWeatherFeatures.class);
        wifiSpotAddress = mock(WifiSpotAddress.class);
        wifiSpotManagement = mock(WifiSpotManagement.class);
        userId = mock(UserId.class);
    }

    @Test
    void testConstructor_withAllFields() {
        WifiSpot wifiSpot = new WifiSpot(
                wifiSpotName,
                wifiSpotDescription,
                wifiSpotCoordinates,
                wifiSpotLocationType,
                wifiSpotQualityIndicators,
                wifiSpotEnvironmentalFeatures,
                wifiSpotFacilities,
                wifiSpotWeatherFeatures,
                wifiSpotAddress,
                wifiSpotManagement,
                userId
        );

        assertNotNull(wifiSpot);
        assertEquals(wifiSpotName, wifiSpot.getWifiSpotName());
        assertEquals(wifiSpotDescription, wifiSpot.getWifiSpotDescription());
        assertEquals(wifiSpotCoordinates, wifiSpot.getWifiSpotCoordinates());
        assertEquals(wifiSpotLocationType, wifiSpot.getWifiSpotLocationType());
        assertEquals(wifiSpotQualityIndicators, wifiSpot.getWifiSpotQualityIndicators());
        assertEquals(wifiSpotEnvironmentalFeatures, wifiSpot.getWifiSpotEnvironmentalFeatures());
        assertEquals(wifiSpotFacilities, wifiSpot.getWifiSpotFacilities());
        assertEquals(wifiSpotWeatherFeatures, wifiSpot.getWifiSpotWeatherFeatures());
        assertEquals(wifiSpotAddress, wifiSpot.getWifiSpotAddress());
        assertEquals(wifiSpotManagement, wifiSpot.getWifiSpotManagement());
    }

    @Test
    void testConstructor_withOnlyRequiredFields() {
        WifiSpot wifiSpot = new WifiSpot(
                new WifiSpotName("Test Spot"),
                new WifiSpotDescription("A cool Wi-Fi spot."),
                new WifiSpotCoordinates(10.0, 20.0),
                new WifiSpotLocationType(LocationType.CAFE),
                new WifiSpotQualityIndicators(
                        QualityType.HIGH,
                        StrengthType.STRONG,
                        BandwithType.LIMITED,
                        new WifiSpotPeakUsageInterval()
                ),
                new WifiSpotEnvironmentalFeatures(true, true, true, true, true, NoiseType.LOUD, true, true, true),
                new WifiSpotFacilities(true, true, true, true, true, true),
                new WifiSpotWeatherFeatures(true, true, true, true, true),
                new WifiSpotAddress(
                        new WifiSpotAddressCountry("Portugal"),
                        new WifiSpotAddressZipCode("2232-111"),
                        new WifiSpotAddressLine1("123 Test St"),
                        null,
                        new WifiSpotAddressCity("Gondomar"),
                        new WifiSpotAddressDistrict("Porto")
                ),
                new WifiSpotManagement(WifiSpotManagementType.UNVERIFIED),
                new UserId()
        );

        assertNotNull(wifiSpot);
        assertEquals("Test Spot", wifiSpot.getWifiSpotName().getValue());
        assertEquals("A cool Wi-Fi spot.", wifiSpot.getWifiSpotDescription().getValue());
        assertEquals(10.0, wifiSpot.getWifiSpotCoordinates().getLongitude());
        assertEquals(20.0, wifiSpot.getWifiSpotCoordinates().getLatitude());
    }
}