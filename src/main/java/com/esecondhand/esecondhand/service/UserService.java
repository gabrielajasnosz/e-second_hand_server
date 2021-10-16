package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.AppUser;
import com.esecondhand.esecondhand.domain.User;
import com.esecondhand.esecondhand.dto.RegisterDto;
import com.esecondhand.esecondhand.dto.UserDto;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import com.esecondhand.esecondhand.mapper.UserMapper;
import com.esecondhand.esecondhand.repository.UserRepository;
import com.esecondhand.esecondhand.security.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userDao;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder bcryptEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserMapper userMapper;

    public UserService(UserRepository userDao, AuthenticationManager authenticationManager, PasswordEncoder bcryptEncoder, JwtTokenUtil jwtTokenUtil, UserMapper userMapper) {
        this.userDao = userDao;
        this.authenticationManager = authenticationManager;
        this.bcryptEncoder = bcryptEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new AppUser(user);
    }

    public User save(RegisterDto userDto) throws EmailAlreadyExistsException {
        if (userDao.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Provided email already exists!");
        }
        Date date = new Date();
        userDto.setCreationDate(date);
        User user = userMapper.mapRegisterUserDtoToUser(userDto);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public String signIn(UserDto userDto) throws Exception {

        authenticate(userDto.getEmail(), userDto.getPassword());
        AppUser userDetails = (AppUser) loadUserByUsername(userDto.getEmail());
        return jwtTokenUtil.generateToken(userDetails);

    }
}
