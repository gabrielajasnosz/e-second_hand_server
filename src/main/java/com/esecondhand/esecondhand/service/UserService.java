package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.*;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.entity.VerificationToken;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User save(RegisterDto userDto) throws EmailAlreadyExistsException;

    String signIn(LoginDto loginDto) throws Exception;

    void createVerificationToken(User user, String token);

    void saveRegisteredUser(User user);

    VerificationToken getVerificationToken(String verificationToken);

    List<UserPreviewDto> findUsers(String name);
    void setUserProfilePicture(MultipartFile file) throws IOException;

    void editProfile(UserDto userDto);

    FileSystemResource findProfilePicture(Long userId) throws ObjectDoesntExistsException;

    UserDto findUser(Long id) throws ObjectDoesntExistsException;

    void changePassword(PasswordEntryDto passwordEntryDto) throws Exception;
}
