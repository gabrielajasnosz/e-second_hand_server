package com.esecondhand.esecondhand.dto;

import com.esecondhand.esecondhand.domain.Category;
import com.esecondhand.esecondhand.domain.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryDto {
    private int id;
    private String name;
    private Gender gender;
    private Integer parentId;
    private List<CategoryDto> subCategories=new ArrayList<>();
}


