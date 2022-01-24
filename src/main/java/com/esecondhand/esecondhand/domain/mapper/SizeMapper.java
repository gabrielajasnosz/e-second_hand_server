package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.ColorDto;
import com.esecondhand.esecondhand.domain.entity.Color;
import com.esecondhand.esecondhand.domain.entity.Size;
import com.esecondhand.esecondhand.domain.dto.SizeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SizeMapper {
    public List<SizeDto> mapToSizeDtoList(List<Size> entity) {
        return entity.stream().map(this::mapToSizeDto).collect(Collectors.toList());
    }

    public SizeDto mapToSizeDto(Size entity){
        SizeDto dto = new SizeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setProductType(entity.getProductType());
        return dto;
    }
}
