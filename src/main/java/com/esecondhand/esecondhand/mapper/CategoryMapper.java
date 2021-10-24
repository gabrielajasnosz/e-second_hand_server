package com.esecondhand.esecondhand.mapper;

import com.esecondhand.esecondhand.domain.Category;
import com.esecondhand.esecondhand.dto.CategoryDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public List<CategoryDto> mapToCategoryDto(List<Category> entity) {
        return entity.stream().map(e -> {
            CategoryDto dto = new CategoryDto();
            dto.setId(e.getId());
            dto.setGender(e.getGender());
            dto.setName(e.getName());
            dto.setSubCategories(mapToCategoryDto(e.getSubCategories()));
            dto.setParentId(e.getParentId() == null ? null : e.getParentId().getId());
            return dto;
        }).collect(Collectors.toList());
    }
}
