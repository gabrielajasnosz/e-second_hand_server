package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CountersDto {
    private Long itemsCounter;
    private Long hiddenItemsCounter;
    private Long commentsCounter;
    private Long followersCounter;
    private Long followingCounter;
}
