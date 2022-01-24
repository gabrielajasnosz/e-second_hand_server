package com.esecondhand.esecondhand.domain.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditItemDto {
    @NotNull(message = "Brand cannot be null")
    @NotEmpty
    private String brand;
    @NotNull(message = "Category cannot be null")
    @NotEmpty
    private String category;
    @NotNull(message = "Color cannot be null")
    @NotEmpty
    private String color;
    private String description;

    @NotNull(message = "Gender cannot be null")
    @NotEmpty
    private String gender;

    @NotNull(message = "Item id cannot be null")
    private Long itemId;

    @NotNull(message = "Name cannot be null")
    @NotEmpty
    private String name;

    @NotNull(message = "Brand cannot be null")
    private Double price;

    @NotNull(message = "Size cannot be null")
    @NotEmpty
    private String size;

}
