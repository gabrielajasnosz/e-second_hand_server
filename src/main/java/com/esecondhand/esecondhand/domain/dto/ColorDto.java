package com.esecondhand.esecondhand.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorDto {
    private Long id;
    private String name;
    private String hexCode;
}