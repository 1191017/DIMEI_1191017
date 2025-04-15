package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Test
    void testDefaultConstructor() {
        // Act
        Username username = new Username();

        // Assert
        assertEquals("", username.getUserName());
    }

    @Test
    void testConstructorWithValue() {
        // Arrange
        String name = "user123";

        // Act
        Username username = new Username(name);

        // Assert
        assertEquals(name, username.getUserName());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Username name1 = new Username("user123");
        Username name2 = new Username("user123");
        Username name3 = new Username("differentUser");

        // Assert
        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
        assertNotEquals(name1, name3);
    }

    @Test
    void testToString() {
        // Arrange
        Username username = new Username("user123");
        String expectedString = "Username(userName=user123)";

        // Act
        String resultString = username.toString();

        // Assert
        assertEquals(expectedString, resultString);
    }
}

