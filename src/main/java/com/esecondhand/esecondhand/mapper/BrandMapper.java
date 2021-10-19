package com.esecondhand.esecondhand.mapper;

import com.esecondhand.esecondhand.domain.Brand;
import com.esecondhand.esecondhand.dto.BrandDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandMapper {
    public List<BrandDto> mapToBrandDtoList(List<Brand> entity) {
        return entity.stream().map(e -> {
            BrandDto dto = new BrandDto();
            dto.setId(e.getId());
            dto.setName(e.getName());
            return dto;
        }).collect(Collectors.toList());
    }
}
