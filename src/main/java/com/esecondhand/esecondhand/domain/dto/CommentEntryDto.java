package com.esecondhand.esecondhand.domain.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntryDto {

    @NotNull
    private int rating;

    @NotEmpty
    private String comment;

    @NotNull
    private Long receiverId;
}
