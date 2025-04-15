package com.netquest.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

public class SignUpRequest {

    @Schema(example = "user3")
    @NotBlank
    private String username;

    @Schema(example = "user3")
    @NotBlank
    private String password;

    @Schema(example = "John")
    @NotBlank
    private String firstName;

    @Schema(example = "Doe")
    @NotBlank
    private String lastName;

    @Schema(example = "MALE")
    @NotBlank
    private String gender; // Enum: MALE, FEMALE, OTHER

    @Schema(example = "user3@mycompany.com")
    @Email
    private String email;

    @Schema(example = "USER")
    @NotBlank
    private String role; // Enum: ADMIN, OFFER_MANAGER, USER, USER_PREMIUM

    @Schema(example = "1990-01-01")
    @NotBlank
    private String birthDate;

    @Schema(example = "123456789")
    private String vatNumber;

    @Schema(example = "123 Main St")
    private String addressLine1;

    @Schema(example = "Apt 4B")
    private String addressLine2;

    @Schema(example = "New York")
    private String city;

    @Schema(example = "New York County")
    private String district;

    @Schema(example = "USA")
    private String country;

    @Schema(example = "10001")
    private String zipCode;


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getCountry() {
        return country;
    }

    public String getZipCode() {
        return zipCode;
    }
}
