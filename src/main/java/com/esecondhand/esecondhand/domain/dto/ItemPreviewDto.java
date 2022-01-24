package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemPreviewDto {

    private Long id;
    private String name;
    private Long userId;
    private String userDisplayName;
    private Double price;
    private LocalDateTime creationDate;
    private Long mainImageId;


}
