package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.User;
import com.esecondhand.esecondhand.dto.UserDto;
import com.esecondhand.esecondhand.mapper.UserDtoMapper;
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

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userDao;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder bcryptEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDtoMapper userDtoMapper;

    public UserService(UserRepository userDao, AuthenticationManager authenticationManager, PasswordEncoder bcryptEncoder, JwtTokenUtil jwtTokenUtil, UserDtoMapper userDtoMapper) {
        this.userDao = userDao;
        this.authenticationManager = authenticationManager;
        this.bcryptEncoder = bcryptEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDtoMapper = userDtoMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<>());
    }

    public User save(UserDto userDto) {
        User user = userDtoMapper.mapUserDtoToUser(userDto);
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

        authenticate(userDto.getUsername(), userDto.getPassword());
        UserDetails userDetails = loadUserByUsername(userDto.getUsername());
        return jwtTokenUtil.generateToken(userDetails);

    }
}
