package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.domain.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryDto {
    private Long id;
    private String name;
    private Gender gender;
    private Long parentId;
    private List<CategoryDto> subCategories=new ArrayList<>();
}


