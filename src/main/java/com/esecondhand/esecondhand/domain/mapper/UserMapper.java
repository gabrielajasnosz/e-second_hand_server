package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.dto.RegisterDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapRegisterUserDtoToUser(RegisterDto userDto) {
        User user = new User();

        user.setPassword(userDto.getPassword());
        user.setDisplayName(userDto.getDisplayName());
        user.setEmail(userDto.getEmail());
        user.setGender(Gender.valueOf(userDto.getSex()));
        user.setCreationDate(userDto.getCreationDate());

        return user;
    }
}
