package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserFirstNameTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserFirstName userFirstName = new UserFirstName();

        // Assert
        assertEquals("", userFirstName.getFirstName());
    }

    @Test
    void testConstructorWithValue() {
        // Arrange
        String name = "John";

        // Act
        UserFirstName userFirstName = new UserFirstName(name);

        // Assert
        assertEquals(name, userFirstName.getFirstName());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UserFirstName name1 = new UserFirstName("Alice");
        UserFirstName name2 = new UserFirstName("Alice");
        UserFirstName name3 = new UserFirstName("Bob");

        // Assert
        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
        assertNotEquals(name1, name3);
    }

    @Test
    void testToString() {
        // Arrange
        UserFirstName userFirstName = new UserFirstName("Alice");
        String expectedString = "UserFirstName(firstName=Alice)";

        // Act
        String resultString = userFirstName.toString();

        // Assert
        assertEquals(expectedString, resultString);
    }
}

