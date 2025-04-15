package com.netquest.infrastructure.runner;

import com.netquest.domain.user.model.*;
import com.netquest.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        if (!userService.getUsers().isEmpty()) {
            return;
        }
        USERS.forEach(userService::saveUser);
        log.info("Database initialized");
    }

    private static final List<User> USERS = Arrays.asList(
            new User(
                    new UserFirstName("AdminFirstName"),
                    new UserLastName("AdminLastName"),
                    UserGender.MALE, // Example gender
                    new Username("admin"),
                    new UserPassword("admin"),
                    new UserMail("admin@mycompany.com"),
                    new UserBirthDate(LocalDate.of(1980, 1, 1)),
                    UserRole.ADMIN,
                    new UserAddress(
                            "123 Admin St",
                            "Suite 100",
                            "AdminCity",
                            "AdminDistrict",
                            "AdminCountry",
                            "12345"
                    ),
                    new UserVATNumber("123456789")
            ),
            new User(
                    new UserFirstName("UserFirstName"),
                    new UserLastName("UserLastName"),
                    UserGender.FEMALE,
                    new Username("user"),
                    new UserPassword("user"),
                    new UserMail("user@mycompany.com"),
                    new UserBirthDate(LocalDate.of(1990, 1, 1)),
                    UserRole.USER,
                    new UserAddress(
                            "456 User St",
                            "Suite 200",
                            "UserCity",
                            "UserDistrict",
                            "UserCountry",
                            "54321"
                    ),
                    new UserVATNumber("987654321")
            )
    );



}
