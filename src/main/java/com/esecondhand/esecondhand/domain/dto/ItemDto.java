package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.domain.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private String name;
    private Long userId;
    private String description;
    private CategoryDto categoryDto;
    private Long brandId;
    private Long colorId;
    private BigDecimal price;
    private Long sizeId;
    private Gender gender;
    private Date creationDate;
}
