package com.esecondhand.esecondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesByGenderDto {
    private List<CategoryDto> woman;
    private List<CategoryDto> man;
}
