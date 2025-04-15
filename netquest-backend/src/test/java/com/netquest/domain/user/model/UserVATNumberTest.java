package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserVATNumberTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserVATNumber userVATNumber = new UserVATNumber();

        // Assert
        assertNull(userVATNumber.getVatNumber());
    }

    @Test
    void testConstructorWithValue() {
        // Arrange
        String vat = "PT123456789";

        // Act
        UserVATNumber userVATNumber = new UserVATNumber(vat);

        // Assert
        assertEquals(vat, userVATNumber.getVatNumber());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UserVATNumber vat1 = new UserVATNumber("PT123456789");
        UserVATNumber vat2 = new UserVATNumber("PT123456789");
        UserVATNumber vat3 = new UserVATNumber("PT987654321");

        // Assert
        assertEquals(vat1, vat2);
        assertEquals(vat1.hashCode(), vat2.hashCode());
        assertNotEquals(vat1, vat3);
    }

    @Test
    void testToString() {
        // Arrange
        UserVATNumber userVATNumber = new UserVATNumber("PT123456789");
        String expectedString = "UserVATNumber(vatNumber=PT123456789)";

        // Act
        String resultString = userVATNumber.toString();

        // Assert
        assertEquals(expectedString, resultString);
    }
}

