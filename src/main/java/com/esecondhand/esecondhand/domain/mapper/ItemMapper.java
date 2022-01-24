package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.dto.ItemPreviewDto;
import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.entity.Item;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    private final ItemPictureMapper itemPictureMapper;

    public ItemMapper(ItemPictureMapper itemPictureMapper) {
        this.itemPictureMapper = itemPictureMapper;
    }

    public Item mapToItem(ItemEntryDto itemEntryDto) {
        return Item.builder()
                .name(itemEntryDto.getName())
                .price(itemEntryDto.getPrice())
                .creationDate(LocalDateTime.now())
                .description(itemEntryDto.getDescription())
                .gender(Gender.valueOf(itemEntryDto.getSex().toUpperCase()))
                .build();

    }

    public ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .userId(item.getUser().getId())
                .userDisplayName(item.getUser().getDisplayName())
                .description(item.getDescription())
                .category(item.getCategory().getName())
                .categoryId(item.getCategory().getId())
                .brand(item.getBrand().getName())
                .brandId(item.getBrand().getId())
                .color(item.getColor().getName())
                .colorId(item.getColor().getId())
                .categoryGender(item.getCategory().getGender())
                .price(item.getPrice())
                .size(item.getSize().getName())
                .productType(item.getSize().getProductType())
                .sizeId(item.getSize().getId())
                .name(item.getName())
                .gender(item.getGender())
                .creationDate(item.getCreationDate())
                .isActive(item.getIsActive())
                .isHidden(item.getIsHidden())
                .itemPictures(itemPictureMapper.mapToItemPictureDtoList(item.getItemPictures()))
                .build();

    }

    public List<ItemPreviewDto> mapToPreviewList(List<Item> itemList, Map<Long, Long> mainPictureIdByItemId) {
        return itemList.stream().map(e -> ItemPreviewDto.builder()
                .creationDate(e.getCreationDate())
                .id(e.getId())
                .name(e.getName())
                .price(e.getPrice())
                .userDisplayName(e.getUser().getDisplayName())
                .userId(e.getUser().getId())
                .mainImageId(mainPictureIdByItemId.get(e.getId()))
                .build()).collect(Collectors.toList());
    }
}
