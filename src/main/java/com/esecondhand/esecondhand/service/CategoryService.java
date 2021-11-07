package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.BrandDto;
import com.esecondhand.esecondhand.domain.dto.CategoryDto;
import com.esecondhand.esecondhand.domain.dto.ColorDto;
import com.esecondhand.esecondhand.domain.dto.SizeDto;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<CategoryDto> getCategories();

    List<BrandDto> getBrands();

    Map<Long, List<SizeDto>> getSizes();

    List<ColorDto> getColors();
}
