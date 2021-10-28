package com.esecondhand.esecondhand.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntryDto {

    @NotNull(message = "Name cannot be null")
    private String name;
    private String description;
    @NotNull(message = "Category id cannot be null")
    private Long categoryId;

    @NotNull(message = "Brand cannot be null")
    private String brand;

    @NotNull(message = "Color cannot be null")
    private Long colorId;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @NotNull(message = "Size cannot be null")
    private Long sizeId;

    @NotNull(message = "Gender cannot be null")
    private String sex;

    private Long mainImageId;

    @NotNull(message = "You have to send at least one photo")
    private MultipartFile[] files;

}
