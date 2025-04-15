package com.netquest.wifispot;

import com.netquest.domain.wifispot.model.WifiSpotName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WifiSpotNameTest {

    @Test
    void testConstructor_withNullValue_shouldThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new WifiSpotName(null);
        });

        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    void testConstructor_withEmptyString_shouldThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new WifiSpotName("");
        });

        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    void testConstructor_withBlankString_shouldThrowException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new WifiSpotName("   "); // Spaces only
        });

        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    void testConstructor_withValidName_shouldPass() {
        WifiSpotName wifiSpotName = new WifiSpotName("Test Spot");

        assertNotNull(wifiSpotName);
        assertEquals("Test Spot", wifiSpotName.getValue());
    }
}
