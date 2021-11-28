package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.LoginDto;
import com.esecondhand.esecondhand.domain.dto.RegisterDto;
import com.esecondhand.esecondhand.domain.dto.UserDto;
import com.esecondhand.esecondhand.domain.dto.UserPreviewDto;
import com.esecondhand.esecondhand.domain.entity.AppUser;
import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.entity.VerificationToken;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.mapper.UserMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import com.esecondhand.esecondhand.security.JwtTokenUtil;
import com.esecondhand.esecondhand.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

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

    private final CommentRepository commentRepository;


    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder bcryptEncoder, JwtTokenUtil jwtTokenUtil, UserMapper userMapper, VerificationTokenRepository verificationTokenRepository, ItemRepository itemRepository, ItemPictureRepository itemPictureRepository, ItemMapper itemMapper, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.bcryptEncoder = bcryptEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
        this.verificationTokenRepository = verificationTokenRepository;
        this.itemRepository = itemRepository;
        this.itemPictureRepository = itemPictureRepository;
        this.itemMapper = itemMapper;
        this.commentRepository = commentRepository;
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
    public void setUserProfilePicture(MultipartFile file) throws IOException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String MAIN_DIR = "src/main/resources/images/";
        String SUB_DIR = appUser.getUser().getId().toString() + "/profile-picture/";

        String FILE_LOCATION = SUB_DIR + appUser.getUser().getId().toString() + ".png";
        Path newFile = Paths.get(MAIN_DIR + FILE_LOCATION);
        Files.createDirectories(newFile.getParent());

        Files.deleteIfExists(newFile);
        Files.write(newFile, file.getBytes());
        User user = appUser.getUser();

        user.setProfilePictureLocation(FILE_LOCATION);
        userRepository.save(user);

    }

    @Override
    public void editProfile(UserDto userDto) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = appUser.getUser();

        if (StringUtils.isBlank(userDto.getCity())) {
            user.setCity(null);
        } else {
            user.setCity(userDto.getCity());
        }

        if (StringUtils.isBlank(userDto.getZipCode())) {
            user.setZipCode(null);
        } else {
            user.setZipCode(userDto.getZipCode());
        }

        if (StringUtils.isBlank(userDto.getPhoneNumber())) {
            user.setPhoneNumber(null);
        } else {
            user.setPhoneNumber(userDto.getPhoneNumber());
        }

        user.setGender(Gender.valueOf(userDto.getGender().toUpperCase()));

        userRepository.save(user);
    }

    @Override
    public FileSystemResource findProfilePicture(Long userId) throws ObjectDoesntExistsException {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new ObjectDoesntExistsException("There is no image for provided user id.");
        }
        String location = "";
        if(user.getProfilePictureLocation() == null){
            location="empty-avatar.png";
        }
        else{
            location=user.getProfilePictureLocation();
        }
        return findInFileSystem(location);
    }

    @Override
    public UserDto findUser(Long id) throws ObjectDoesntExistsException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null || !user.isEnabled()) {
            throw new ObjectDoesntExistsException("User with provided id doesn't exist");
        }
        UserDto userDto = userMapper.mapToUserDto(user);
        userDto.setRating(commentRepository.findUserAvgRating(id));

        return userDto;

    }

    private FileSystemResource findInFileSystem(String location) {
        String MAIN_DIR = "src/main/resources/images/";
        try {
            return new FileSystemResource(Paths.get(MAIN_DIR + location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
