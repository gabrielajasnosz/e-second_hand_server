package com.esecondhand.esecondhand.dto;

import com.esecondhand.esecondhand.domain.Gender;
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
    private Long categoryId;
    private Long brandId;
    private Long colorId;
    private BigDecimal price;
    private Long sizeId;
    private Gender gender;
    private Date creationDate;
}
