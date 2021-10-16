package com.esecondhand.esecondhand.service;


import com.esecondhand.esecondhand.domain.MainCategory;
import com.esecondhand.esecondhand.dto.MainCategoryDto;
import com.esecondhand.esecondhand.mapper.CategoryMapper;
import com.esecondhand.esecondhand.repository.MainCategoryRepository;
import com.esecondhand.esecondhand.repository.SubcategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private MainCategoryRepository mainCategoryRepository;

    private CategoryMapper categoryMapper;

    private SubcategoryRepository subcategoryRepository;

    public CategoryService(MainCategoryRepository mainCategoryRepository, SubcategoryRepository subcategoryRepository, CategoryMapper categoryMapper) {
        this.mainCategoryRepository = mainCategoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.categoryMapper = categoryMapper;
    }

    private List<MainCategoryDto> getSubcategories(List<MainCategoryDto> list, String condition) {
        return list.stream().filter(category -> category.getType().equals("condition")).collect(Collectors.toList());
    }

    public Map<String, Map<String, List<MainCategoryDto>>> getCategories() {

        List<MainCategory> mainCategories = mainCategoryRepository.findAll();

        List<MainCategoryDto> mainCategoryDtos = categoryMapper.mapToCategoryDto(mainCategories);

        List<MainCategoryDto> categories = mainCategoryDtos.stream().map(dto -> {
            dto.setSubcategories(categoryMapper.mapToSubcategoryDto(subcategoryRepository.findAllByMainCategoryId(dto.getId())));
            return dto;
        }).collect(Collectors.toList());

        return categories.stream().collect(Collectors.groupingBy(MainCategoryDto::getType, Collectors.groupingBy(MainCategoryDto::getDestinationSex)));

    }
}
