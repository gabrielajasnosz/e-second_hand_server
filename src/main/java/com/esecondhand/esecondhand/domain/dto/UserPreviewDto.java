package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserPreviewDto {
    private Long id;
    private String displayName;
}
