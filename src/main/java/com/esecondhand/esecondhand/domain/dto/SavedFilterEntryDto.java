package com.esecondhand.esecondhand.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SavedFilterEntryDto {
    private Long categoryId;
    private Long brandId;
    private Long colorId;
    private Long sizeId;
    private Double maxPrice;
    private Double minPrice;
    private String sortingColumn;
    private String sortingOrder;
    private String name;
    private String gender;
}

