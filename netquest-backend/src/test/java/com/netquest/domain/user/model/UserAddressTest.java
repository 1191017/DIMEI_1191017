package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserAddressTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String addressLine1 = "123 Main St";
        String addressLine2 = "Apt 4B";
        String city = "Springfield";
        String district = "Some District";
        String country = "USA";
        String zipCode = "12345";

        // Act
        UserAddress address = new UserAddress(addressLine1, addressLine2, city, district, country, zipCode);

        // Assert
        assertEquals(addressLine1, address.getAddressLine1());
        assertEquals(addressLine2, address.getAddressLine2());
        assertEquals(city, address.getCity());
        assertEquals(district, address.getDistrict());
        assertEquals(country, address.getCountry());
        assertEquals(zipCode, address.getZipCode());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        UserAddress address = new UserAddress();

        // Assert
        assertNull(address.getAddressLine1());
        assertNull(address.getAddressLine2());
        assertNull(address.getCity());
        assertNull(address.getDistrict());
        assertNull(address.getCountry());
        assertNull(address.getZipCode());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UserAddress address1 = new UserAddress("123 Main St", "Apt 4B", "Springfield", "Some District", "USA", "12345");
        UserAddress address2 = new UserAddress("123 Main St", "Apt 4B", "Springfield", "Some District", "USA", "12345");
        UserAddress address3 = new UserAddress("456 Elm St", "Apt 5C", "Shelbyville", "Another District", "USA", "67890");

        // Assert
        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
        assertNotEquals(address1, address3);
    }

    @Test
    void testToString() {
        // Arrange
        UserAddress address = new UserAddress("123 Main St", "Apt 4B", "Springfield", "Some District", "USA", "12345");
        String expectedString = "UserAddress(addressLine1=123 Main St, addressLine2=Apt 4B, city=Springfield, district=Some District, country=USA, zipCode=12345)";

        // Act
        String resultString = address.toString();

        // Assert
        assertEquals(expectedString, resultString);
    }
}

