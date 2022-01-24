package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.ItemPictureDto;
import com.esecondhand.esecondhand.domain.entity.ItemPicture;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemPictureMapper {

    public List<ItemPictureDto> mapToItemPictureDtoList(List<ItemPicture> itemPictures) {
        return itemPictures.stream().map(e -> {
            ItemPictureDto dto = new ItemPictureDto();
            dto.setId(e.getId());
            dto.setFileUrl(e.getFileUrl());
            dto.setMainPicture(e.isMainPicture());
            return dto;
        }).collect(Collectors.toList());
    }

}
