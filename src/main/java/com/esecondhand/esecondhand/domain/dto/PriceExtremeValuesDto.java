package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PriceExtremeValuesDto {
    private Double maxPrice;
    private Double minPrice;
}
