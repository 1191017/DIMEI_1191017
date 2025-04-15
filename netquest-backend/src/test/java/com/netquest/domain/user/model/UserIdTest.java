package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void testConstructorWithUUID() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        UserId userId = new UserId(uuid);

        // Assert
        assertEquals(uuid, userId.getValue());
    }

    @Test
    void testDefaultConstructor() {
        // Act
        UserId userId1 = new UserId();
        UserId userId2 = new UserId();

        // Assert
        assertNotNull(userId1.getValue());
        assertNotNull(userId2.getValue());
        assertNotEquals(userId1.getValue(), userId2.getValue()); // Ensures new random UUIDs
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        UserId userId1 = new UserId(uuid);
        UserId userId2 = new UserId(uuid);
        UserId userId3 = new UserId(UUID.randomUUID());

        // Assert
        assertEquals(userId1, userId2);
        assertEquals(userId1.hashCode(), userId2.hashCode());
        assertNotEquals(userId1, userId3);
    }

    @Test
    void testToString() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        UserId userId = new UserId(uuid);
        String expectedString = "UserId(value=" + uuid + ")";

        // Act
        String resultString = userId.toString();

        // Assert
        assertEquals(expectedString, resultString);
    }
}

