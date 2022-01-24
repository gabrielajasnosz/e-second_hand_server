package com.esecondhand.esecondhand.service.serviceImpl;


import com.esecondhand.esecondhand.domain.dto.BrandDto;
import com.esecondhand.esecondhand.domain.dto.CategoryDto;
import com.esecondhand.esecondhand.domain.dto.ColorDto;
import com.esecondhand.esecondhand.domain.dto.NewCategoryDto;
import com.esecondhand.esecondhand.domain.entity.Category;
import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.mapper.BrandMapper;
import com.esecondhand.esecondhand.domain.mapper.CategoryMapper;
import com.esecondhand.esecondhand.domain.mapper.ColorMapper;
import com.esecondhand.esecondhand.domain.mapper.SizeMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryMapper categoryMapper;

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getCategories() {
        List <Category> categories = categoryRepository.findAllCategories();

        return categoryMapper.mapToCategoryDtoList(categories);

    }

    @Override
    public void saveCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        Category parentCategory = categoryRepository.findById(newCategoryDto.getParentId()).orElse(null);
        if(parentCategory != null){
            category.setParentId(parentCategory);
            if(!parentCategory.getGender().toString().equals("UNDEFINED")){
                category.setGender(parentCategory.getGender());
            } else {
                category.setGender(Gender.valueOf(newCategoryDto.getCategoryGender()));
            }
            category.setName(newCategoryDto.getCategoryName());
            categoryRepository.save(category);
        }
    }

}
