package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.ChatMessagesDto;
import com.esecondhand.esecondhand.domain.dto.MessageDto;
import com.esecondhand.esecondhand.domain.dto.MessageEntryDto;
import com.esecondhand.esecondhand.domain.dto.MessagePreviewDto;
import com.esecondhand.esecondhand.domain.entity.Message;
import com.esecondhand.esecondhand.service.MessageService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class MessageController {


    Logger logger = LoggerFactory.getLogger(MessageController.class);
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/add")
    public void postMessage(MessageEntryDto messageEntryDto) {
        messageService.saveMessage(messageEntryDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @GetMapping("/all")
    public ResponseEntity<List<MessagePreviewDto>> getAllMessages() {
        List<MessagePreviewDto> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @GetMapping("/chat")
    public ResponseEntity<ChatMessagesDto> getChatMessages(@RequestParam("chatId") Long chatId) {
        return ResponseEntity.ok(messageService.getChatMessages(chatId));
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @GetMapping("/unread-counter")
    public ResponseEntity<?> getMessagesCounter() {
        return ResponseEntity.ok().body(messageService.getMessagesCounter());
    }
}
