package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.ColorDto;
import com.esecondhand.esecondhand.domain.entity.Color;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ColorMapper {
    public List<ColorDto> mapToColorDtoList(List<Color> entity) {
        return entity.stream().map(this::mapToColorDto).collect(Collectors.toList());
    }

    public ColorDto mapToColorDto(Color entity) {
        return ColorDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .hexCode(entity.getHexCode()).build();
    }
}
