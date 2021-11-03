package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.domain.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private Long userId;
    private String userDisplayName;
    private String description;
    private String category;
    private Long categoryId;
    private Gender categoryGender;
    private String brand;
    private String color;
    private Double price;
    private String size;
    private Gender gender;
    private Date creationDate;
    private List<ItemPictureDto> itemPictures;



}
