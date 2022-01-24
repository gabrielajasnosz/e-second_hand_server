package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.domain.entity.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavedFilterDto {
    private Long id;
    private CategoryDto categoryDto;
    private BrandDto brandDto;
    private ColorDto colorDto;
    private SizeDto sizeDto;
    private Double maxPrice;
    private Double minPrice;
    private String sortingColumn;
    private String sortingOrder;
    private String name;
    private String gender;
}
