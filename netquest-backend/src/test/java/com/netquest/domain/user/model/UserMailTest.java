package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserMailTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserMail userMail = new UserMail();

        // Assert
        assertEquals("", userMail.getMail());
    }

    @Test
    void testConstructorWithValue() {
        // Arrange
        String email = "test@example.com";

        // Act
        UserMail userMail = new UserMail(email);

        // Assert
        assertEquals(email, userMail.getMail());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UserMail mail1 = new UserMail("alice@example.com");
        UserMail mail2 = new UserMail("alice@example.com");
        UserMail mail3 = new UserMail("bob@example.com");

        // Assert
        assertEquals(mail1, mail2);
        assertEquals(mail1.hashCode(), mail2.hashCode());
        assertNotEquals(mail1, mail3);
    }

    @Test
    void testToString() {
        // Arrange
        UserMail userMail = new UserMail("alice@example.com");
        String expectedString = "UserMail(mail=alice@example.com)";

        // Act
        String resultString = userMail.toString();

        // Assert
        assertEquals(expectedString, resultString);
    }
}

