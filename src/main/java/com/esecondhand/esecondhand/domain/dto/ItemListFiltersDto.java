package com.esecondhand.esecondhand.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemListFiltersDto {
    private Long categoryId;
    private String gender;
    private Long brandId;
    private Long userId;
    private Long colorId;
    private Long sizeId;
    private String sortingColumn;
    private String sortingOrder;
    private Long nextItemId;
    private String nextItemValue;
    private Integer pageSize;
    private Double maxPrice;
    private Double minPrice;
    private boolean onlyFollowedUsers;

}
