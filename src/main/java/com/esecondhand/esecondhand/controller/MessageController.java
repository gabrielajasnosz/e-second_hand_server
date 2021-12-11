package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.MessageDto;
import com.esecondhand.esecondhand.domain.dto.MessageEntryDto;
import com.esecondhand.esecondhand.domain.dto.MessagePreviewDto;
import com.esecondhand.esecondhand.domain.entity.Message;
import com.esecondhand.esecondhand.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/messages")
public class MessageController {


    Logger logger = LoggerFactory.getLogger(MessageController.class);
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/add")
    @SendTo("/topic/message")
    public Message postMessage(MessageEntryDto messageEntryDto) {
        logger.debug("-------------------------------------");
        return messageService.saveMessage(messageEntryDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MessagePreviewDto>> getAllMessages() {
        List<MessagePreviewDto> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }
    @GetMapping("/chat")
    public ResponseEntity<List<MessageDto>> getChatMessages(@RequestParam("chatId") Long chatId) {
        List<MessageDto> messages = messageService.getChatMessages(chatId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unread-counter")
    public ResponseEntity<?> getMessagesCounter() {
        return ResponseEntity.ok().body(messageService.getMessagesCounter());
    }
}
