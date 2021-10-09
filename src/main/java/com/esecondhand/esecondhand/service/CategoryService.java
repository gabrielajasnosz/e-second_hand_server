package com.esecondhand.esecondhand.service;


import com.esecondhand.esecondhand.domain.MainCategory;
import com.esecondhand.esecondhand.dto.MainCategoryDto;
import com.esecondhand.esecondhand.mapper.CategoryMapper;
import com.esecondhand.esecondhand.repository.MainCategoryRepository;
import com.esecondhand.esecondhand.repository.SubcategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private MainCategoryRepository mainCategoryRepository;

    private CategoryMapper categoryMapper;

    private SubcategoryRepository subcategoryRepository;

    private final String WOMEN_CATEGORIES = "female";

    public CategoryService(MainCategoryRepository mainCategoryRepository, SubcategoryRepository subcategoryRepository, CategoryMapper categoryMapper) {
        this.mainCategoryRepository = mainCategoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<MainCategoryDto> getWomenCategories(){

        List<MainCategory> mainCategories = mainCategoryRepository.findAllByDestinationSex(WOMEN_CATEGORIES);

        List<MainCategoryDto> mainCategoryDtos = categoryMapper.mapToCategoryDto(mainCategories);

         return mainCategoryDtos.stream().map(dto -> {
            dto.setSubcategories(categoryMapper.mapToSubcategoryDto(subcategoryRepository.findAllByMainCategoryId(dto.getId())));
            return dto;
        }).collect(Collectors.toList());
    }
}
