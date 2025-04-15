package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserLastNameTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserLastName userLastName = new UserLastName();

        // Assert
        assertEquals("", userLastName.getLastName());
    }

    @Test
    void testConstructorWithValue() {
        // Arrange
        String name = "Doe";

        // Act
        UserLastName userLastName = new UserLastName(name);

        // Assert
        assertEquals(name, userLastName.getLastName());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UserLastName name1 = new UserLastName("Smith");
        UserLastName name2 = new UserLastName("Smith");
        UserLastName name3 = new UserLastName("Johnson");

        // Assert
        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
        assertNotEquals(name1, name3);
    }

    @Test
    void testToString() {
        // Arrange
        UserLastName userLastName = new UserLastName("Smith");
        String expectedString = "UserLastName(lastName=Smith)";

        // Act
        String resultString = userLastName.toString();

        // Assert
        assertEquals(expectedString, resultString);
    }
}
