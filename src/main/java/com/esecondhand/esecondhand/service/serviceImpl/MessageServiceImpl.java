package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.MessageDto;
import com.esecondhand.esecondhand.domain.dto.MessageEntryDto;
import com.esecondhand.esecondhand.domain.dto.MessagePreviewDto;
import com.esecondhand.esecondhand.domain.entity.AppUser;
import com.esecondhand.esecondhand.domain.entity.Chat;
import com.esecondhand.esecondhand.domain.entity.Message;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.mapper.MessageMapper;
import com.esecondhand.esecondhand.domain.repository.ChatRepository;
import com.esecondhand.esecondhand.domain.repository.MessageRepository;
import com.esecondhand.esecondhand.domain.repository.UserRepository;
import com.esecondhand.esecondhand.service.MessageService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    private final ChatRepository chatRepository;

    private final MessageMapper messageMapper;

    public MessageServiceImpl(UserRepository userRepository, MessageRepository messageRepository, ChatRepository chatRepository, MessageMapper messageMapper) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public Message saveMessage(MessageEntryDto messageEntryDto) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Message message = new Message();
        message.setCreationDate(LocalDateTime.now());
        User receiver = userRepository.findById(messageEntryDto.getReceiverId()).orElse(null);
        message.setSeen(false);

        return messageRepository.save(message);
    }

    @Override
    public List<MessagePreviewDto> getAllMessages() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Chat> chatIds = chatRepository.findChatsByParticipant(appUser.getUser().getId());
        List<MessagePreviewDto> messagePreviewDtos = chatIds.stream().map(chat -> {
            Message message = messageRepository.findMessagePreviewByChatId(chat.getId());
            if(message != null){
                MessagePreviewDto messagePreviewDto = messageMapper.mapToMessagePreviewDto(message);
                if(appUser.getUser().getId().equals(chat.getFirstParticipant().getId())){
                    messagePreviewDto.setConversationalistId(chat.getSecondParticipant().getId());
                    messagePreviewDto.setConversationalist(chat.getSecondParticipant().getDisplayName());
                } else {
                    messagePreviewDto.setConversationalistId(chat.getFirstParticipant().getId());
                    messagePreviewDto.setConversationalist(chat.getFirstParticipant().getDisplayName());
                }
                messagePreviewDto.setChatId(chat.getId());
                return messagePreviewDto;
            }
            return null;
        }).collect(Collectors.toList());
        return messagePreviewDtos;
    }

    @Override
    public List<MessageDto> getChatMessages(Long chatId) {
        List<Message> messages = messageRepository.findAllByChatIdOrderByCreationDateAsc(chatId);
        return messageMapper.mapMessagesList(messages);
    }

}
