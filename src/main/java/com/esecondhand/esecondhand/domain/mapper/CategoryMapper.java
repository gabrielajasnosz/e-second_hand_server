package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.CategoryDto;
import com.esecondhand.esecondhand.domain.entity.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public List<CategoryDto> mapToCategoryDtoList(List<Category> entity) {
        return entity.stream().map(this::mapToCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto mapToCategoryDto(Category category) {

        return CategoryDto.builder()
                .id(category.getId())
                .gender(category.getGender())
                .name(category.getName())
                .subCategories(mapToCategoryDtoList(category.getSubCategories()))
                .parentId(category.getParentId() == null ? null : category.getParentId().getId())
                .build();
    }
}
