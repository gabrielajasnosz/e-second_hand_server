package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.ChatMessagesDto;
import com.esecondhand.esecondhand.domain.dto.MessageDto;
import com.esecondhand.esecondhand.domain.dto.MessageEntryDto;
import com.esecondhand.esecondhand.domain.dto.MessagePreviewDto;
import com.esecondhand.esecondhand.domain.entity.AppUser;
import com.esecondhand.esecondhand.domain.entity.Chat;
import com.esecondhand.esecondhand.domain.entity.ChatParticipant;
import com.esecondhand.esecondhand.domain.entity.Message;
import com.esecondhand.esecondhand.domain.mapper.MessageMapper;
import com.esecondhand.esecondhand.domain.repository.ChatParticipantRepository;
import com.esecondhand.esecondhand.domain.repository.ChatRepository;
import com.esecondhand.esecondhand.domain.repository.MessageRepository;
import com.esecondhand.esecondhand.domain.repository.UserRepository;
import com.esecondhand.esecondhand.service.MessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    private final ChatRepository chatRepository;

    private final MessageMapper messageMapper;

    private final ChatParticipantRepository chatParticipantRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;


    public MessageServiceImpl(UserRepository userRepository, MessageRepository messageRepository, ChatRepository chatRepository, MessageMapper messageMapper, ChatParticipantRepository chatParticipantRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.messageMapper = messageMapper;
        this.chatParticipantRepository = chatParticipantRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void saveMessage(MessageEntryDto messageEntryDto) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Message message = new Message();
        if (messageEntryDto.getChatId() != null) {
            ChatParticipant chatParticipant = chatParticipantRepository.findChatParticipant(appUser.getUser().getId(), messageEntryDto.getChatId());
            message.setSeen(false);
            message.setCreationDate(LocalDateTime.now());
            message.setAuthor(userRepository.findById(appUser.getUser().getId()).orElse(null));
            message.setContent(messageEntryDto.getMessage());
            message.setChat(chatParticipant.getChat());
        } else {
            Chat chat = chatRepository.save(new Chat());
            ChatParticipant creator = new ChatParticipant();
            ChatParticipant receiver = new ChatParticipant();
            receiver.setChat(chat);
            receiver.setParticipant(userRepository.findById(messageEntryDto.getReceiverId()).orElse(null));
            chatParticipantRepository.save(receiver);
            creator.setChat(chat);
            creator.setParticipant(appUser.getUser());
            chatParticipantRepository.save(creator);
            message.setSeen(false);
            message.setCreationDate(LocalDateTime.now());
            message.setAuthor(appUser.getUser());
            message.setContent(messageEntryDto.getMessage());
            message.setChat(chat);
            simpMessagingTemplate.convertAndSendToUser(appUser.getUser().getId().toString(), "/newChat", chat);
        }
        Message newMessage = messageRepository.save(message);
        MessageDto messageDto = messageMapper.mapToMessageDto(newMessage);
        messageDto.setChatId(message.getChat().getId());
        List<MessageDto> list = new ArrayList<>();
        list.add(messageDto);
        ChatParticipant chatParticipant = chatParticipantRepository.findChatParticipant(appUser.getUser().getId(), messageDto.getChatId());
        simpMessagingTemplate.convertAndSendToUser(chatParticipant.getParticipant().getId().toString(), "/newNotification", list);
        simpMessagingTemplate.convertAndSendToUser(newMessage.getChat().getId().toString(), "/newMessage", list);
        System.out.println("lol");
    }

    @Override
    public List<MessagePreviewDto> getAllMessages() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<ChatParticipant> chatIds = chatParticipantRepository.findAllByParticipantId(appUser.getUser().getId());
        List<MessagePreviewDto> messagePreviewDtos = chatIds.stream().map(chatParticipant -> {
            Message message = messageRepository.findMessagePreviewByChatId(chatParticipant.getChat().getId());
            if (message != null) {
                MessagePreviewDto messagePreviewDto = messageMapper.mapToMessagePreviewDto(message);
                ChatParticipant conversationalist = chatParticipantRepository.findChatParticipant(appUser.getUser().getId(), chatParticipant.getChat().getId());
                Chat chat = chatRepository.findById(chatParticipant.getChat().getId()).orElse(null);
                messagePreviewDto.setConversationalistId(conversationalist.getParticipant().getId());
                messagePreviewDto.setConversationalist(conversationalist.getParticipant().getDisplayName());
                messagePreviewDto.setChatId(chatParticipant.getChat().getId());
                if (chat != null) {
                    messagePreviewDto.setChatName(chat.getChatName());
                }
                return messagePreviewDto;
            }
            return null;
        }).collect(Collectors.toList());
        return messagePreviewDtos;
    }

    @Override
    public ChatMessagesDto getChatMessages(Long chatId) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Message> messages = messageRepository.findAllByChatIdOrderByCreationDateAsc(chatId);
        List<Message> messagesToChangeStatus = messages.stream().filter((e) -> !e.getAuthor().getId().equals(appUser.getUser().getId()) && !e.isSeen()).collect(Collectors.toList());
        messagesToChangeStatus.forEach((m) -> {
            m.setSeen(true);
            messageRepository.save(m);
        });
        ChatMessagesDto chatMessagesDto = new ChatMessagesDto();
        chatMessagesDto.setMessageDtoList(messageMapper.mapMessagesList(messages));
        ChatParticipant chatParticipant = chatParticipantRepository.findChatParticipant(appUser.getUser().getId(), chatId);
        chatMessagesDto.setChatUserId(chatParticipant.getParticipant().getId());
        chatMessagesDto.setChatUserName(chatParticipant.getParticipant().getDisplayName());
        return chatMessagesDto;
    }

    @Override
    public Long getMessagesCounter() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Long> userChatIds = chatParticipantRepository.findUserChats(appUser.getUser().getId());
        return messageRepository.findUnreadCounter(userChatIds, appUser.getUser().getId());
    }

}
