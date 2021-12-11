package com.esecondhand.esecondhand.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessagePreviewDto {
    private String conversationalist;
    private Long conversationalistId;
    private Long lastMessageAuthorId;
    private String lastMessage;
    private boolean isLastMessageSeen;
    private Long chatId;
    private String chatName;
}
