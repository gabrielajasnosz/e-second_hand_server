package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemPictureDto;
import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ItemMapper {

    private ItemPictureMapper itemPictureMapper;

    public ItemMapper(ItemPictureMapper itemPictureMapper) {
        this.itemPictureMapper = itemPictureMapper;
    }

    public Item mapToItem(ItemEntryDto itemEntryDto) {

        Item item = new Item();
        item.setName(itemEntryDto.getName());
        item.setDescription(itemEntryDto.getDescription());
        item.setCreationDate(new Date());
        item.setPrice(itemEntryDto.getPrice());
        item.setGender(Gender.valueOf(itemEntryDto.getSex().toUpperCase()));

        return item;

    }

    public ItemDto mapToItemDto(Item item) {


        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setUserId(item.getUser().getId());
        itemDto.setUserDisplayName(item.getUser().getDisplayName());
        itemDto.setDescription(item.getDescription());
        itemDto.setCategory(item.getCategory().getName());
        itemDto.setCategoryId(item.getCategory().getId());
        itemDto.setCategoryGender(item.getCategory().getGender());
        itemDto.setBrand(item.getBrand().getName());
        itemDto.setBrandId(item.getBrand().getId());
        itemDto.setColor(item.getColor().getName());
        itemDto.setColorId(item.getColor().getId());
        itemDto.setPrice(item.getPrice());
        itemDto.setSize(item.getSize().getName());
        itemDto.setProductType(item.getSize().getProductType());
        itemDto.setSizeId(item.getSize().getId());
        itemDto.setName(item.getName());
        itemDto.setGender(item.getGender());
        itemDto.setCreationDate(item.getCreationDate());
        itemDto.setIsActive(item.getIsActive());
        itemDto.setIsHidden(item.getIsHidden());
        itemDto.setItemPictures(itemPictureMapper.mapToItemPictureDtoList(item.getItemPictures()));


        return itemDto;

    }
}
