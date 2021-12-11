package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.MessageDto;
import com.esecondhand.esecondhand.domain.dto.MessageEntryDto;
import com.esecondhand.esecondhand.domain.dto.MessagePreviewDto;
import com.esecondhand.esecondhand.domain.entity.Message;

import java.util.List;

public interface MessageService {
    Message saveMessage(MessageEntryDto messageEntryDto);
    List<MessagePreviewDto> getAllMessages();
    List<MessageDto> getChatMessages(Long chatId);
    Long getMessagesCounter();
}
