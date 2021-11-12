package com.esecondhand.esecondhand.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPreviewDto {

    private Long id;
    private String name;
    private Long userId;
    private String userDisplayName;
    private Double price;
    private LocalDateTime creationDate;
    private Long mainImageId;


}
