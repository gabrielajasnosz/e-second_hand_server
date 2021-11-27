package com.esecondhand.esecondhand.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntryDto {

    @NotNull
    private int rating;

    @NotEmpty
    private String comment;

    @NotNull
    private Long receiverId;
}
