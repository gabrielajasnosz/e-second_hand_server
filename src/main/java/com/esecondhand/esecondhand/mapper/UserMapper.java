package com.esecondhand.esecondhand.mapper;

import com.esecondhand.esecondhand.domain.User;
import com.esecondhand.esecondhand.dto.RegisterDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapRegisterUserDtoToUser(RegisterDto userDto) {
        User user = new User();
        user.setPassword(userDto.getPassword());
        user.setDisplayName(userDto.getDisplayName());
        user.setEmail(userDto.getEmail());

        return user;
    }
}
