package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.entity.Category;
import com.esecondhand.esecondhand.domain.dto.CategoryDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public List<CategoryDto> mapToCategoryDtoList(List<Category> entity) {
        return entity.stream().map(this::mapToCategoryDto).collect(Collectors.toList());
    }
    public CategoryDto mapToCategoryDto(Category category){
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setGender(category.getGender());
        dto.setName(category.getName());
        dto.setSubCategories(mapToCategoryDtoList(category.getSubCategories()));
        dto.setParentId(category.getParentId() == null ? null : category.getParentId().getId());

        return dto;
    }
}
