package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.BrandDto;
import com.esecondhand.esecondhand.domain.entity.Brand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BrandMapper {
    public List<BrandDto> mapToBrandDtoList(List<Brand> entity) {
        return entity.stream().map(e -> mapToBrandDto(e)).collect(Collectors.toList());
    }

    public BrandDto mapToBrandDto(Brand entity) {
        return BrandDto.builder().id(entity.getId()).name(entity.getName()).build();
    }
}
