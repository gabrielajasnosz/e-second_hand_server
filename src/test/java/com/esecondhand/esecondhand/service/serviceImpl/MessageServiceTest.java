package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.MessageEntryDto;
import com.esecondhand.esecondhand.domain.entity.*;
import com.esecondhand.esecondhand.domain.repository.ChatParticipantRepository;
import com.esecondhand.esecondhand.domain.repository.ChatRepository;
import com.esecondhand.esecondhand.domain.repository.MessageRepository;
import com.esecondhand.esecondhand.domain.repository.UserRepository;
import com.esecondhand.esecondhand.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatParticipantRepository chatParticipantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private Clock clock;
    private Clock fixedClock;

    User loggedUser;
    User secondUser;
    User thirdUser;
    User fourthUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loggedUser = userRepository.save(createAppUser("logged", "logged@mail.com"));
        secondUser = userRepository.save(createAppUser("second", "second@mail.com"));
        thirdUser = userRepository.save(createAppUser("third", "third@mail.com"));
        fourthUser = userRepository.save(createAppUser("fourth", "fourth@mail.com"));



    }


    @Test
    public void shouldCreateNewChatWhenDoesntExist() {
        //given
        User loggedUser = userRepository.save(createAppUser("logged", "logged@mail.com"));
        User secondUser = userRepository.save(createAppUser("second", "second@mail.com"));
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when((AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(new AppUser(loggedUser));
        MessageEntryDto messageEntryDto = new MessageEntryDto(null, secondUser.getId(), "test message");

        //when
        messageService.saveMessage(messageEntryDto);

        //then
        Chat chat = chatRepository.findChat(loggedUser.getId(), secondUser.getId());
        assertNotNull(chat);
        assertNotNull(chatParticipantRepository.findChatParticipant(loggedUser.getId(), chat.getId()));
        assertNotNull(chatParticipantRepository.findChatParticipant(messageEntryDto.getReceiverId(), chat.getId()));
    }


    @Test
    public void shouldSaveNewMessage() {

        Chat chat = chatRepository.save(new Chat());
        ChatParticipant loggedChatParticipant = chatParticipantRepository.save(new ChatParticipant(null, chat, loggedUser));
        ChatParticipant thirdChatParticipant = chatParticipantRepository.save(new ChatParticipant(null, chat, thirdUser));

        MessageEntryDto messageEntryDto = new MessageEntryDto(chat.getId(), thirdChatParticipant.getId(), "message");

        messageService.saveMessage(messageEntryDto);

        List<Message> messages = messageRepository.findAllByChatIdOrderByCreationDateAsc(chat.getId());
        assertNotNull(messages);
        assertEquals(messages.size(), 1);
        Message message = messages.get(0);
        assertEquals(message.getAuthor(), loggedUser);
        assertEquals(message.getChat(), chat);
        assertEquals(message.getContent(), messageEntryDto.getMessage());

    }

    @Test
    public void shouldGetCorrectCounter() {

        Chat chat = chatRepository.save(new Chat());
        ChatParticipant thirdChatParticipant = chatParticipantRepository.save(new ChatParticipant(null, chat, thirdUser));

        MessageEntryDto messageEntryDto = new MessageEntryDto(chat.getId(), thirdChatParticipant.getId(), "message");

        messageService.saveMessage(messageEntryDto);

        List<Message> messages = messageRepository.findAllByChatIdOrderByCreationDateAsc(chat.getId());
        assertNotNull(messages);
        assertEquals(messages.size(), 1);
        Message message = messages.get(0);
        assertEquals(message.getAuthor(), loggedUser);
        assertEquals(message.getChat(), chat);
        assertEquals(message.getContent(), messageEntryDto.getMessage());

    }


    private User createAppUser(String displayName, String email) {
        User user = new User();
        user.setRole("USER");
        user.setPassword("test");
        user.setGender(Gender.valueOf("MAN"));
        user.setZipCode("33-100");
        user.setEnabled(true);
        user.setCreationDate(LocalDateTime.now());
        user.setDisplayName(displayName);
        user.setEmail(email);
        return user;
    }


}
