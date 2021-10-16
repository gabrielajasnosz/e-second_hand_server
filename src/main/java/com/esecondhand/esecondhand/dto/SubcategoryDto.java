package com.esecondhand.esecondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryDto {
    private int id;
    private String name;
    private int mainCategoryId;

}
