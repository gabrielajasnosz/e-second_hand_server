package com.esecondhand.esecondhand.domain.mapper;
import com.esecondhand.esecondhand.domain.dto.ColorDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import com.esecondhand.esecondhand.domain.entity.Color;

@Component
public class ColorMapper {
    public List<ColorDto> mapToColorDtoList(List<Color> entity) {
        return entity.stream().map(e -> {
            ColorDto dto = new ColorDto();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setHexCode(e.getHexCode());
            return dto;
        }).collect(Collectors.toList());
    }
}
