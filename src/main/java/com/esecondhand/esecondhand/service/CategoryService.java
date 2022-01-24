package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.*;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<CategoryDto> getCategories();
    void saveCategory(NewCategoryDto newCategoryDto);

}
