package com.esecondhand.esecondhand.mapper;

import com.esecondhand.esecondhand.domain.User;
import com.esecondhand.esecondhand.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public User mapUserDtoToUser(UserDto userDto) {
        User user = new User();
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());

        return user;
    }
}
