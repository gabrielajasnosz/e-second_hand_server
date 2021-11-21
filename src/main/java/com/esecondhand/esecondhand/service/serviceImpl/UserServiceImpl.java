package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.*;
import com.esecondhand.esecondhand.domain.entity.AppUser;
import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.entity.VerificationToken;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.mapper.UserMapper;
import com.esecondhand.esecondhand.domain.repository.ItemPictureRepository;
import com.esecondhand.esecondhand.domain.repository.ItemRepository;
import com.esecondhand.esecondhand.domain.repository.UserRepository;
import com.esecondhand.esecondhand.domain.repository.VerificationTokenRepository;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import com.esecondhand.esecondhand.exception.ItemDoesntExistsException;
import com.esecondhand.esecondhand.security.JwtTokenUtil;
import com.esecondhand.esecondhand.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private static final int EXPIRATION = 60 * 24;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder bcryptEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserMapper userMapper;

    private final VerificationTokenRepository verificationTokenRepository;

    private final ItemRepository itemRepository;

    private final ItemPictureRepository itemPictureRepository;

    private final ItemMapper itemMapper;


    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder bcryptEncoder, JwtTokenUtil jwtTokenUtil, UserMapper userMapper, VerificationTokenRepository verificationTokenRepository, ItemRepository itemRepository, ItemPictureRepository itemPictureRepository, ItemMapper itemMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.bcryptEncoder = bcryptEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
        this.verificationTokenRepository = verificationTokenRepository;
        this.itemRepository = itemRepository;
        this.itemPictureRepository = itemPictureRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.isEnabled()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new AppUser(user);
    }

    public User save(RegisterDto userDto) throws EmailAlreadyExistsException {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Provided email already exists!");
        }
        userDto.setCreationDate(LocalDateTime.now());
        User user = userMapper.mapRegisterUserDtoToUser(userDto);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        return userRepository.save(user);
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

    public String signIn(LoginDto loginDto) throws Exception {

        authenticate(loginDto.getEmail(), loginDto.getPassword());
        AppUser userDetails = (AppUser) loadUserByUsername(loginDto.getEmail());
        return jwtTokenUtil.generateToken(userDetails);

    }

    private LocalDateTime calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);

        return LocalDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(calculateExpiryDate());
        verificationTokenRepository.save(myToken);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken);
    }

    @Override
    public List<UserPreviewDto> findUsers(String name) {
        String keyword = name.trim().toUpperCase();
        List<User> users = userRepository.findByDisplayNameIgnoreCaseContaining(keyword);
        return userMapper.mapUserList(users);
    }

    @Override
    public UserDto findUser(Long id) throws ItemDoesntExistsException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = userRepository.findById(id).orElse(null);
        if (user == null || !user.isEnabled()) {
            throw new ItemDoesntExistsException("User with provided id doesn't exist");
        }
        UserDto userDto = userMapper.mapToUserDto(user);

        return userDto;

    }
}
