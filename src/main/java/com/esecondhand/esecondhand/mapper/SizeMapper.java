package com.esecondhand.esecondhand.mapper;

import com.esecondhand.esecondhand.domain.Size;
import com.esecondhand.esecondhand.dto.SizeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SizeMapper {
    public List<SizeDto> mapToSizeDtoList(List<Size> entity) {
        return entity.stream().map(e -> {
            SizeDto dto = new SizeDto();
            dto.setId(e.getId());
            dto.setName(e.getName());
            dto.setProductType(e.getProductType());
            return dto;
        }).collect(Collectors.toList());
    }
}