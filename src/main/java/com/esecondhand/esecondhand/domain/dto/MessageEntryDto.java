package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageEntryDto {
    private Long chatId;
    private Long receiverId;
    private String message;
}
