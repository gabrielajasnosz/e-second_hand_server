package com.esecondhand.esecondhand.domain.dto;

import com.esecondhand.esecondhand.domain.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemListDto {
    private List<ItemPreviewDto> itemList;
    private Long nextItemId;
    private Object nextItemValue;
}
