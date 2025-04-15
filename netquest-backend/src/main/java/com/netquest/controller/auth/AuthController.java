package com.netquest.controller.auth;

import com.netquest.domain.DuplicatedUserInfoException;
import com.netquest.domain.user.model.*;
import com.netquest.domain.auth.dto.AuthResponse;
import com.netquest.domain.auth.dto.LoginRequest;
import com.netquest.domain.auth.dto.SignUpRequest;
import com.netquest.security.WebSecurityConfig;
import com.netquest.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.validUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(new AuthResponse(user.getUserId().getValue(), user.getUsername().getUserName(), user.getRole().name()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public AuthResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.hasUserWithUsername(signUpRequest.getUsername())) {
            throw new DuplicatedUserInfoException(String.format("Username %s is already been used", signUpRequest.getUsername()));
        }
        if (userService.hasUserWithEmail(signUpRequest.getEmail())) {
            throw new DuplicatedUserInfoException(String.format("Email %s is already been used", signUpRequest.getEmail()));
        }

        User user = userService.saveUser(createUser(signUpRequest));
        return new AuthResponse(user.getUserId().getValue(), user.getUsername().getUserName(), user.getRole().name());
    }

    private User createUser(SignUpRequest signUpRequest) {
        return new User( // Automatically generated UUID
                new UserFirstName(signUpRequest.getFirstName()),
                new UserLastName(signUpRequest.getLastName()),
                UserGender.valueOf(signUpRequest.getGender()),
                new Username(signUpRequest.getUsername()),
                new UserPassword(signUpRequest.getPassword()),
                new UserMail(signUpRequest.getEmail()),
                new UserBirthDate(signUpRequest.getBirthDate()),
                UserRole.valueOf(signUpRequest.getRole()),
                new UserAddress(
                        signUpRequest.getAddressLine1(),
                        signUpRequest.getAddressLine2(),
                        signUpRequest.getCity(),
                        signUpRequest.getDistrict(),
                        signUpRequest.getCountry(),
                        signUpRequest.getZipCode()
                ),
                new UserVATNumber(signUpRequest.getVatNumber())
        );
    }
}
