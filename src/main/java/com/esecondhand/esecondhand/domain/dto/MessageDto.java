package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.domain.entity.Chat;
import com.esecondhand.esecondhand.domain.entity.User;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private Long authorId;

    private String content;

    private LocalDateTime creationDate;

    private Long chatId;

}
