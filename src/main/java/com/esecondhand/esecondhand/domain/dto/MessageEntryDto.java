package com.esecondhand.esecondhand.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntryDto {
    private Long authorId;
    private Long chatId;
    private Long receiverId;
    private String message;
}
