package com.esecondhand.esecondhand.domain.mapper;
import com.esecondhand.esecondhand.domain.dto.ColorDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import com.esecondhand.esecondhand.domain.entity.Color;

@Component
public class ColorMapper {
    public List<ColorDto> mapToColorDtoList(List<Color> entity) {
        return entity.stream().map(this::mapToColorDto).collect(Collectors.toList());
    }
    public ColorDto mapToColorDto(Color entity){
        ColorDto dto = new ColorDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setHexCode(entity.getHexCode());
        return dto;
    }
}
