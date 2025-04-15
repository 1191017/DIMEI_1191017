package com.netquest.domain.user.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String gender,
        String password,
        String email,
        String role,
        LocalDate birthDate,
        String vatNumber,
        String addressLine1,
        String addressLine2,
        String addressCity,
        String addressDistrict,
        String addressCountry,
        String addressZipCode
) {
}