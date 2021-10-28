package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.RegisterDto;
import com.esecondhand.esecondhand.domain.dto.UserDto;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User save(RegisterDto userDto) throws EmailAlreadyExistsException;

    String signIn(UserDto userDto) throws Exception;
}
