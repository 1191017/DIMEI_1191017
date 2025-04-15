package com.netquest.domain.user.mapper;

import com.netquest.domain.user.model.User;
import com.netquest.domain.user.dto.UserDto;

public interface UserMapper {

    UserDto toUserDto(User user);
}