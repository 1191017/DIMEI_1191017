package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UserBirthDateTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserBirthDate birthDate = new UserBirthDate();

        // Assert
        assertEquals(LocalDate.now(), birthDate.getBirthdate());
    }

    @Test
    void testConstructorWithLocalDate() {
        // Arrange
        LocalDate date = LocalDate.of(1990, 5, 15);

        // Act
        UserBirthDate birthDate = new UserBirthDate(date);

        // Assert
        assertEquals(date, birthDate.getBirthdate());
    }

    @Test
    void testConstructorWithValidString() {
        // Arrange
        String dateStr = "1990-05-15";
        LocalDate expectedDate = LocalDate.of(1990, 5, 15);

        // Act
        UserBirthDate birthDate = new UserBirthDate(dateStr);

        // Assert
        assertEquals(expectedDate, birthDate.getBirthdate());
    }

    @Test
    void testConstructorWithInvalidString() {
        // Arrange
        String invalidDateStr = "15-05-1990";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new UserBirthDate(invalidDateStr));
        assertEquals("Invalid date format. Expected format is yyyy-MM-dd.", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UserBirthDate birthDate1 = new UserBirthDate(LocalDate.of(1990, 5, 15));
        UserBirthDate birthDate2 = new UserBirthDate(LocalDate.of(1990, 5, 15));
        UserBirthDate birthDate3 = new UserBirthDate(LocalDate.of(1985, 8, 20));

        // Assert
        assertEquals(birthDate1, birthDate2);
        assertEquals(birthDate1.hashCode(), birthDate2.hashCode());
        assertNotEquals(birthDate1, birthDate3);
    }

    @Test
    void testToString() {
        // Arrange
        UserBirthDate birthDate = new UserBirthDate(LocalDate.of(1990, 5, 15));
        String expectedString = "UserBirthDate(birthdate=1990-05-15)";

        // Act
        String resultString = birthDate.toString();

        // Assert
        assertEquals(expectedString, resultString);
    }
}
