package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private Long creatorId;
    private String creatorName;
    private int rating;
    private String comment;
    private LocalDateTime creationDate;
}
