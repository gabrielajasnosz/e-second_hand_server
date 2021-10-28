package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ItemMapper {

    public Item mapToItem(ItemEntryDto itemEntryDto) {

        Item item = new Item();
        item.setName(itemEntryDto.getName());
        item.setDescription(itemEntryDto.getDescription());
        item.setCreationDate(new Date());
        item.setPrice(itemEntryDto.getPrice());
        item.setGender(Gender.valueOf(itemEntryDto.getSex().toUpperCase()));

        return item;

    }
}
