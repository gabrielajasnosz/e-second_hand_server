package com.esecondhand.esecondhand.dto;

import lombok.*;

import java.util.Collection;


@Data
public class MainCategoryDto {
    private int id;
    private String type;
    private String destinationSex;
    private String name;
    private Collection<SubcategoryDto> subcategories;


}
