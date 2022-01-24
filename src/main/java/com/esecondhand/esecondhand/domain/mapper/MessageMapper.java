package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.MessageDto;
import com.esecondhand.esecondhand.domain.dto.MessagePreviewDto;
import com.esecondhand.esecondhand.domain.entity.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {

    public MessagePreviewDto mapToMessagePreviewDto(Message message) {
        MessagePreviewDto messagePreviewDto = new MessagePreviewDto();
        messagePreviewDto.setLastMessage(message.getContent());
        messagePreviewDto.setLastMessageAuthorId(message.getAuthor().getId());
        messagePreviewDto.setLastMessageSeen(message.isSeen());
        return messagePreviewDto;
    }

    public List<MessageDto> mapMessagesList(List<Message> messages){
        return messages.stream().map(this::mapToMessageDto).collect(Collectors.toList());
    }
    public MessageDto mapToMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setAuthorId(message.getAuthor().getId());
        messageDto.setContent(message.getContent());
        messageDto.setCreationDate(message.getCreationDate());
        return messageDto;
    }
}
