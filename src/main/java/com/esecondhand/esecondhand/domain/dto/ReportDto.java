package com.esecondhand.esecondhand.domain.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReportDto {
    private Long itemId;
    private String cause;
}
