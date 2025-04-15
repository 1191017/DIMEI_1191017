package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserPasswordTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserPassword userPassword = new UserPassword();

        // Assert
        assertEquals("", userPassword.getPassword());
    }

    @Test
    void testConstructorWithValue() {
        // Arrange
        String password = "securePassword123";

        // Act
        UserPassword userPassword = new UserPassword(password);

        // Assert
        assertEquals(password, userPassword.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UserPassword password1 = new UserPassword("securePassword123");
        UserPassword password2 = new UserPassword("securePassword123");
        UserPassword password3 = new UserPassword("differentPassword");

        // Assert
        assertEquals(password1, password2);
        assertEquals(password1.hashCode(), password2.hashCode());
        assertNotEquals(password1, password3);
    }

    @Test
    void testToString() {
        // Arrange
        UserPassword userPassword = new UserPassword("securePassword123");
        String expectedString = "UserPassword(password=securePassword123)";

        // Act
        String resultString = userPassword.toString();

        // Assert
        assertEquals(expectedString, resultString);
    }
}

