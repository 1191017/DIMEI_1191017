package com.netquest.domain.user.service;

import com.netquest.domain.user.dto.UserDto;
import com.netquest.domain.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> getUsers();

    Optional<User> getUserByUsername(String username);

    boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);

    User validateAndGetUserByUsername(String username);

    User saveUser(User user);

    User getUserById(UUID userUUID);

    void deleteUser(User user);

    void deleteUserById(UUID userUUID);

    Optional<User> validUsernameAndPassword(String username, String password);

    boolean existsById(UUID userUUID);

    void updateUserDetails(UserDto currentUser);
}
