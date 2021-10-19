package com.esecondhand.esecondhand.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private int id;
    private String name;
    private int userId;
    private String description;
    private int categoryId;
    private int brandId;
    private int colorId;
    private double price;
    private int sizeId;
    private Date creationDate;
}
