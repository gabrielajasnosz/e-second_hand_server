package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.UserDto;
import com.esecondhand.esecondhand.domain.dto.UserPreviewDto;
import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.dto.RegisterDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public List<UserPreviewDto> mapUserList(List<User> users){
         return users.stream().map((element)->{
            UserPreviewDto userPreviewDto = new UserPreviewDto();
            userPreviewDto.setDisplayName(element.getDisplayName());
            userPreviewDto.setId(element.getId());
            return userPreviewDto;
        }).collect(Collectors.toList());
    }

    public User mapRegisterUserDtoToUser(RegisterDto userDto) {
        User user = new User();

        user.setPassword(userDto.getPassword());
        user.setDisplayName(userDto.getDisplayName());
        user.setEmail(userDto.getEmail());
        user.setGender(Gender.valueOf(userDto.getSex()));
        user.setCreationDate(userDto.getCreationDate());

        return user;
    }

    public UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setCity(user.getCity());
        userDto.setCreationDate(user.getCreationDate());
        userDto.setDisplayName(user.getDisplayName());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender().toString());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setZipCode(user.getZipCode());
        userDto.setProfilePictureLocation(user.getProfilePictureLocation());

        return userDto;
    }
}
