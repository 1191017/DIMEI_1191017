package com.netquest.domain.user.mapper.impl;

import com.netquest.domain.user.mapper.UserMapper;
import com.netquest.domain.user.model.User;
import com.netquest.domain.user.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getUserId().getValue(),
                user.getUsername().getUserName(),
                user.getFirstName().getFirstName(),
                user.getLastName().getLastName(),
                user.getGender().name(),
                user.getPassword().getPassword(),
                user.getEmail().getMail(),
                user.getRole().name(),
                user.getBirthDate().getBirthdate(),
                user.getVatNumber().toString(),
                user.getAddress().getAddressLine1(),
                user.getAddress().getAddressLine2(),
                user.getAddress().getCity(),
                user.getAddress().getDistrict(),
                user.getAddress().getCountry(),
                user.getAddress().getZipCode()
        );
    }

}
