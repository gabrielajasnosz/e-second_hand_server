package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.domain.entity.Item;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemListDto {
    private List<ItemPreviewDto> itemList;
    private Long nextItemId;
    private Object nextItemValue;
}
