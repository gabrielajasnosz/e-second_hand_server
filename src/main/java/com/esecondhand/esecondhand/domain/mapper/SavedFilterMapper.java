package com.esecondhand.esecondhand.domain.mapper;

import com.esecondhand.esecondhand.domain.dto.SavedFilterDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterPreviewDto;
import com.esecondhand.esecondhand.domain.entity.SavedFilter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SavedFilterMapper {

    private BrandMapper brandMapper;
    private ColorMapper colorMapper;
    private SizeMapper sizeMapper;
    private CategoryMapper categoryMapper;

    public SavedFilterMapper(BrandMapper brandMapper, ColorMapper colorMapper, SizeMapper sizeMapper, CategoryMapper categoryMapper) {
        this.brandMapper = brandMapper;
        this.colorMapper = colorMapper;
        this.sizeMapper = sizeMapper;
        this.categoryMapper = categoryMapper;
    }


    public List<SavedFilterPreviewDto> mapToSavedFilterDtoList(List<SavedFilter> savedFilters) {
        return savedFilters.stream().map(e -> {
            SavedFilterPreviewDto dto = new SavedFilterPreviewDto();
            dto.setId(e.getId());
            dto.setName(e.getName());
            return dto;
        }).collect(Collectors.toList());
    }

    public SavedFilterDto mapToSavedFilterDto(SavedFilter savedFilter) {
        SavedFilterDto dto = new SavedFilterDto();
        if (savedFilter.getBrand() != null) {
            dto.setBrandDto(brandMapper.mapToBrandDto(savedFilter.getBrand()));
        }
        if (savedFilter.getCategory() != null) {
            dto.setCategoryDto(categoryMapper.mapToCategoryDto(savedFilter.getCategory()));
        }
        if (savedFilter.getColor() != null) {
            dto.setColorDto(colorMapper.mapToColorDto(savedFilter.getColor()));
        }
        if (savedFilter.getSize() != null) {
            dto.setSizeDto(sizeMapper.mapToSizeDto(savedFilter.getSize()));
        }
        dto.setMaxPrice(savedFilter.getMaxPrice());
        dto.setMinPrice(savedFilter.getMinPrice());
        dto.setSortingColumn(savedFilter.getSortingColumn());
        dto.setSortingOrder(savedFilter.getSortingOrder());
        dto.setName(savedFilter.getName());
        if (savedFilter.getGender() != null) {
            dto.setGender(savedFilter.getGender().toString());
        }

        return dto;
    }
}
