package com.esecondhand.esecondhand.mapper;

import com.esecondhand.esecondhand.domain.Item;
import com.esecondhand.esecondhand.dto.ItemDto;
import com.esecondhand.esecondhand.dto.ItemEntryDto;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public Item mapToItem(ItemDto itemDto){
        Item item = new Item();
        item.setBrandId(itemDto.getBrandId());
        item.setCategoryId(itemDto.getCategoryId());
        item.setCreationDate(itemDto.getCreationDate());
        item.setDescription(itemDto.getDescription());
        item.setGender(itemDto.getGender());
        item.setPrice(itemDto.getPrice());
        item.setSizeId(itemDto.getSizeId());
        item.setColorId(itemDto.getColorId());
        item.setUserId(itemDto.getUserId());
        item.setName(itemDto.getName());

        return item;

    }
}
