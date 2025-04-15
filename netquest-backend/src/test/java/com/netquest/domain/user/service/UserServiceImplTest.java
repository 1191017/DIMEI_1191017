package com.netquest.domain.user.service;

import com.netquest.domain.user.dto.UserDto;
import com.netquest.domain.user.exception.UserNotFoundException;
import com.netquest.domain.user.model.*;
import com.netquest.domain.user.service.impl.UserServiceImpl;
import com.netquest.infrastructure.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private Username username;
    private UserPassword password;
    private UserMail email;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock data setup
        username = new Username("johndoe");
        password = new UserPassword("password123");
        email = new UserMail("john.doe@example.com");

        user = new User(
                new UserFirstName("John"),
                new UserLastName("Doe"),
                UserGender.MALE,
                username,
                password,
                email,
                new UserBirthDate("1990-01-01"),
                UserRole.USER,
                new UserAddress("123 Street", "Apt 4B", "City", "District", "Country", "12345"),
                new UserVATNumber("VAT123456")
        );
    }

    @Test
    void testGetUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act
        List<User> users = userService.getUsers();

        // Assert
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    void testGetUserByUsername() {
        // Arrange
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.getUserByUsername("johndoe");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetUserByUsernameNotFound() {
        // Arrange
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.validateAndGetUserByUsername("johndoe"));
    }

    @Test
    void testHasUserWithUsername() {
        // Arrange
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean exists = userService.hasUserWithUsername("johndoe");

        // Assert
        assertTrue(exists);
    }

    @Test
    void testHasUserWithEmail() {
        // Arrange
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean exists = userService.hasUserWithEmail("john.doe@example.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    void testDeleteUser() {
        // Act
        userService.deleteUser(user);

        // Assert
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUserById() {
        // Arrange
        UUID userUUID = UUID.randomUUID();
        UserId userId = new UserId(userUUID);
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUserById(userUUID);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetUserById() {
        // Arrange
        UUID userUUID = UUID.randomUUID();
        UserId userId = new UserId(userUUID);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(userUUID);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testValidUsernameAndPassword() {
        // Arrange
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Act
        Optional<User> result = userService.validUsernameAndPassword("johndoe", "password123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testValidUsernameAndPasswordInvalidPassword() {
        // Arrange
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act
        Optional<User> result = userService.validUsernameAndPassword("johndoe", "wrongpassword");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testExistsById() {
        // Arrange
        UUID userUUID = UUID.randomUUID();
        UserId userId = new UserId(userUUID);
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        boolean exists = userService.existsById(userUUID);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testUpdateUserDetails_withPassword() {
        // Given
        UUID userId = UUID.randomUUID();
        String rawPassword = "newPassword123";
        String encodedPassword = "encodedPassword123";

        UserDto currentUser = new UserDto(
                userId,
                "john_doe",
                "John",
                "Doe",
                "Male",
                rawPassword,
                "john.doe@example.com",
                "USER",
                LocalDate.of(1990, 1, 1),
                "VAT123456",
                "Address Line 1",
                "Address Line 2",
                "City",
                "District",
                "Country",
                "12345"
        );

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // When
        userService.updateUserDetails(currentUser);

        // Then
        verify(userRepository).updateUser(
                userId,
                encodedPassword,
                "john.doe@example.com",
                "VAT123456",
                "Address Line 1",
                "Address Line 2",
                "City",
                "District",
                "Country",
                "12345"
        );
    }

    @Test
    void testUpdateUserDetails_withoutPassword() {
        // Given
        UUID userId = UUID.randomUUID();

        UserDto currentUser = new UserDto(
                userId,
                "john_doe",
                "John",
                "Doe",
                "Male",
                null, // No password
                "john.doe@example.com",
                "USER",
                LocalDate.of(1990, 1, 1),
                "123456789",
                "Address Line 1",
                "Address Line 2",
                "City",
                "District",
                "Country",
                "12345"
        );

        // When
        userService.updateUserDetails(currentUser);

        // Then
        verify(userRepository).updateUser(
                userId,
                null,
                "john.doe@example.com",
                "123456789",
                "Address Line 1",
                "Address Line 2",
                "City",
                "District",
                "Country",
                "12345"
        );
    }
}

