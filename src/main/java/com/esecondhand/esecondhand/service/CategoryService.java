package com.esecondhand.esecondhand.service;


import com.esecondhand.esecondhand.domain.MainCategory;
import com.esecondhand.esecondhand.dto.BrandDto;
import com.esecondhand.esecondhand.dto.MainCategoryDto;
import com.esecondhand.esecondhand.dto.SizeDto;
import com.esecondhand.esecondhand.mapper.BrandMapper;
import com.esecondhand.esecondhand.mapper.CategoryMapper;
import com.esecondhand.esecondhand.mapper.SizeMapper;
import com.esecondhand.esecondhand.repository.BrandRepository;
import com.esecondhand.esecondhand.repository.MainCategoryRepository;
import com.esecondhand.esecondhand.repository.SizeRepository;
import com.esecondhand.esecondhand.repository.SubcategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private MainCategoryRepository mainCategoryRepository;

    private CategoryMapper categoryMapper;

    private SizeMapper sizeMapper;

    private SubcategoryRepository subcategoryRepository;

    private BrandRepository brandRepository;

    private SizeRepository sizeRepository;

    private BrandMapper brandMapper;

    public CategoryService(MainCategoryRepository mainCategoryRepository, SubcategoryRepository subcategoryRepository, CategoryMapper categoryMapper, BrandRepository brandRepository, SizeMapper sizeMapper, SizeRepository sizeRepository, BrandMapper brandMapper) {
        this.mainCategoryRepository = mainCategoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.categoryMapper = categoryMapper;
        this.brandRepository = brandRepository;
        this.sizeMapper = sizeMapper;
        this.sizeRepository = sizeRepository;
        this.brandMapper = brandMapper;
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

    public List<BrandDto> getBrands() {

        return brandMapper.mapToBrandDtoList(brandRepository.findAll());

    }
    public Map<String,List<SizeDto>> getSizes() {

        List<SizeDto> sizes = sizeMapper.mapToSizeDtoList(sizeRepository.findAll());

        return sizes.stream().collect(Collectors.groupingBy(SizeDto::getProductType));

    }
}
