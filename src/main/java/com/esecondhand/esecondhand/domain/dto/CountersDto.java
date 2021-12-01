package com.esecondhand.esecondhand.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountersDto {
    private Long itemsCounter;
    private Long hiddenItemsCounter;
    private Long commentsCounter;
    private Long followersCounter;
    private Long followingCounter;
}
