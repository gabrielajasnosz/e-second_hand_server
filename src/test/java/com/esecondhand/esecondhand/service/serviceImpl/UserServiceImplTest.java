package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.RegisterDto;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.mapper.UserMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.exception.EmailAlreadyExistsException;
import com.esecondhand.esecondhand.security.JwtTokenUtil;
import com.esecondhand.esecondhand.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {

    private int EXPIRATION = 60 * 24;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemPictureRepository itemPictureRepository;

    @MockBean
    private ItemMapper itemMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private ChatParticipantRepository chatParticipantRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private Clock clock;
    private Clock fixedClock;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldSaveNewUser() throws EmailAlreadyExistsException {
        RegisterDto registerDto = createRegisterDto("email@email.com", "first user");
        when(userMapper.mapRegisterUserDtoToUser(any())).thenCallRealMethod();

        User user = userService.save(registerDto);
        assertNotNull(user);
        assertEquals(user.getDisplayName(), registerDto.getDisplayName());
        assertEquals(user.getEmail(), registerDto.getEmail());
    }

    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExists() throws EmailAlreadyExistsException {
        when(userMapper.mapRegisterUserDtoToUser(any())).thenCallRealMethod();

        RegisterDto registerDto = createRegisterDto("newEmail@email.com", "second user");
        userService.save(registerDto);

        RegisterDto secondDto = createRegisterDto("newEmail@email.com", "new user");
        Exception exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.save(secondDto);
        });

        String expectedMessage = "Provided email already exists!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    private RegisterDto createRegisterDto(String email, String displayName) {
        return RegisterDto.builder()
                .creationDate(LocalDateTime.now())
                .displayName(displayName)
                .email(email)
                .sex("WOMAN")
                .password("password")
                .build();

    }
}
