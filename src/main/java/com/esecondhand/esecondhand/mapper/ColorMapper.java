package com.esecondhand.esecondhand.mapper;
import com.esecondhand.esecondhand.dto.ColorDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import com.esecondhand.esecondhand.domain.Color;

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
