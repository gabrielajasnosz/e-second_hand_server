package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.domain.entity.Message;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessagesDto {
    private Long chatUserId;
    private String chatUserName;
    private List<MessageDto> messageDtoList;
}
