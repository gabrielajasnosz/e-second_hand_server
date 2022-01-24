package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
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
