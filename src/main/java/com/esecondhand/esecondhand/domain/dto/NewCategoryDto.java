package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NewCategoryDto {
    private Long parentId;
    private String categoryGender;
    private String categoryName;
}
