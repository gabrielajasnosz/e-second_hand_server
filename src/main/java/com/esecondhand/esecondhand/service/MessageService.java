package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.ChatMessagesDto;
import com.esecondhand.esecondhand.domain.dto.MessageDto;
import com.esecondhand.esecondhand.domain.dto.MessageEntryDto;
import com.esecondhand.esecondhand.domain.dto.MessagePreviewDto;
import com.esecondhand.esecondhand.domain.entity.Message;

import java.util.List;

public interface MessageService {
    void saveMessage(MessageEntryDto messageEntryDto);
    List<MessagePreviewDto> getAllMessages();
    ChatMessagesDto getChatMessages(Long chatId);
    Long getMessagesCounter();
}
