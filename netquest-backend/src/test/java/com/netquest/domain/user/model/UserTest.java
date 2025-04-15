package com.netquest.domain.user.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testConstructorWithFields() {
        // Arrange
        UserFirstName firstName = new UserFirstName("John");
        UserLastName lastName = new UserLastName("Doe");
        UserGender gender = UserGender.MALE;
        Username username = new Username("johndoe");
        UserPassword password = new UserPassword("password123");
        UserMail email = new UserMail("john.doe@example.com");
        UserBirthDate birthDate = new UserBirthDate("1990-01-01");
        UserRole role = UserRole.USER;
        UserAddress address = new UserAddress("123 Street", "Apt 4B", "City", "District", "Country", "12345");
        UserVATNumber vatNumber = new UserVATNumber("VAT123456");

        // Act
        User user = new User(firstName, lastName, gender, username, password, email, birthDate, role, address, vatNumber);

        // Assert
        assertNotNull(user.getUserId());
        assertEquals("John", user.getFirstName().getFirstName());
        assertEquals("Doe", user.getLastName().getLastName());
        assertEquals("johndoe", user.getUsername().getUserName());
        assertEquals("john.doe@example.com", user.getEmail().getMail());
        assertEquals("1990-01-01", user.getBirthDate().getBirthdate().toString());
        assertEquals(UserRole.USER, user.getRole());
        assertEquals("12345", user.getAddress().getZipCode());
        assertEquals("VAT123456", user.getVatNumber().getVatNumber());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UserFirstName firstName = new UserFirstName("John");
        UserLastName lastName = new UserLastName("Doe");
        UserGender gender = UserGender.MALE;
        Username username = new Username("johndoe");
        UserPassword password = new UserPassword("password123");
        UserMail email = new UserMail("john.doe@example.com");
        UserBirthDate birthDate = new UserBirthDate("1990-01-01");
        UserRole role = UserRole.USER;
        UserAddress address = new UserAddress("123 Street", "Apt 4B", "City", "District", "Country", "12345");
        UserVATNumber vatNumber = new UserVATNumber("VAT123456");

        User user1 = new User(firstName, lastName, gender, username, password, email, birthDate, role, address, vatNumber);
        User user2 = new User(firstName, lastName, gender, username, password, email, birthDate, role, address, vatNumber);

        // Assert not equals do to random userId
        assertNotEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        UserFirstName firstName = new UserFirstName("John");
        UserLastName lastName = new UserLastName("Doe");
        UserGender gender = UserGender.MALE;
        Username username = new Username("johndoe");
        UserPassword password = new UserPassword("password123");
        UserMail email = new UserMail("john.doe@example.com");
        UserBirthDate birthDate = new UserBirthDate("1990-01-01");
        UserRole role = UserRole.USER;
        UserAddress address = new UserAddress("123 Street", "Apt 4B", "City", "District", "Country", "12345");
        UserVATNumber vatNumber = new UserVATNumber("VAT123456");

        User user = new User(firstName, lastName, gender, username, password, email, birthDate, role, address, vatNumber);

        // Act
        String resultString = user.toString();

        // Assert
        assertTrue(resultString.contains("UserId"));
        assertTrue(resultString.contains("John"));
        assertTrue(resultString.contains("Doe"));
        assertTrue(resultString.contains("johndoe"));
    }

}

