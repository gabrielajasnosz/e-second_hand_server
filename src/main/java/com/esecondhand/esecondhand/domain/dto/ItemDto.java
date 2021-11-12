package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.domain.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private Long userId;
    private String userDisplayName;
    private String description;
    private String category;
    private Long categoryId;
    private Gender categoryGender;
    private String brand;
    private Long brandId;
    private String color;
    private Long colorId;
    private Double price;
    private String size;
    private Long sizeId;
    private Long productType;
    private Gender gender;
    private LocalDateTime creationDate;
    private Boolean isHidden;
    private Boolean isActive;
    private List<ItemPictureDto> itemPictures;
}
