package com.netquest.domain.user.service.impl;

import com.netquest.domain.user.dto.UserDto;
import com.netquest.domain.user.exception.UserNotFoundException;
import com.netquest.domain.user.model.*;
import com.netquest.domain.user.service.UserService;
import com.netquest.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(new Username(username));
    }

    @Override
    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(new Username(username));
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(new UserMail(email));
    }

    @Override
    public User validateAndGetUserByUsername(String username) {
        return getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username %s not found", username)));
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(new UserPassword(passwordEncoder.encode(user.getPassword().getPassword())));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteUserById(UUID userUUID) {
        UserId userId = new UserId(userUUID);
        userRepository.deleteById(userId);
    }

    @Override
    public User getUserById(UUID userUUID) {
        UserId userId = new UserId(userUUID);
        return userRepository.findById(userId).orElseThrow();
    }

    @Override
    public Optional<User> validUsernameAndPassword(String username, String password) {
        return getUserByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword().getPassword()));
    }

    @Override
    public boolean existsById(UUID userUUID) {
        UserId userId = new UserId(userUUID);
        return userRepository.existsById(userId);
    }

    @Override
    public void updateUserDetails(UserDto currentUser){
        String password = null;
        if(currentUser.password() != null){
            password = passwordEncoder.encode(currentUser.password());
        }
        userRepository.updateUser(currentUser.id(),password, currentUser.email(), currentUser.vatNumber(), currentUser.addressLine1(), currentUser.addressLine2(), currentUser.addressCity(), currentUser.addressDistrict(), currentUser.addressCountry(),currentUser.addressZipCode());
    }
}
