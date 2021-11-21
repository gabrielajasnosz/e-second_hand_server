package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.RegisterDto;
import com.esecondhand.esecondhand.domain.dto.LoginDto;
import com.esecondhand.esecondhand.domain.dto.UserDto;
import com.esecondhand.esecondhand.domain.dto.UserPreviewDto;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.entity.VerificationToken;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import com.esecondhand.esecondhand.exception.ItemDoesntExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User save(RegisterDto userDto) throws EmailAlreadyExistsException;

    String signIn(LoginDto loginDto) throws Exception;

    void createVerificationToken(User user, String token);

    void saveRegisteredUser(User user);

    VerificationToken getVerificationToken(String verificationToken);

    List<UserPreviewDto> findUsers(String name);

    UserDto findUser(Long id) throws ItemDoesntExistsException;
}
