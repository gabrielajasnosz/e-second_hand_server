package com.esecondhand.esecondhand.service;


import com.esecondhand.esecondhand.domain.Category;
import com.esecondhand.esecondhand.dto.*;
import com.esecondhand.esecondhand.mapper.BrandMapper;
import com.esecondhand.esecondhand.mapper.CategoryMapper;
import com.esecondhand.esecondhand.mapper.ColorMapper;
import com.esecondhand.esecondhand.mapper.SizeMapper;
import com.esecondhand.esecondhand.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private CategoryMapper categoryMapper;

    private SizeMapper sizeMapper;

    private BrandRepository brandRepository;

    private SizeRepository sizeRepository;

    private BrandMapper brandMapper;

    private ColorMapper colorMapper;

    private ColorRepository colorRepository;
    private CategoryRepository categoryRepository;


    public CategoryService( CategoryMapper categoryMapper, BrandRepository brandRepository, SizeMapper sizeMapper, SizeRepository sizeRepository, BrandMapper brandMapper, ColorMapper colorMapper, ColorRepository colorRepository, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.brandRepository = brandRepository;
        this.sizeMapper = sizeMapper;
        this.sizeRepository = sizeRepository;
        this.brandMapper = brandMapper;
        this.colorMapper = colorMapper;
        this.colorRepository = colorRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getCategories() {
        List <Category> categories = categoryRepository.findAllCategories();
        List <CategoryDto> categoriesDto = categoryMapper.mapToCategoryDto(categories);

        return categoriesDto;

    }

    public List<BrandDto> getBrands() {

        return brandMapper.mapToBrandDtoList(brandRepository.findAll());

    }
    public Map<String,List<SizeDto>> getSizes() {

        List<SizeDto> sizes = sizeMapper.mapToSizeDtoList(sizeRepository.findAll());

        return sizes.stream().collect(Collectors.groupingBy(SizeDto::getProductType));

    }
    public List<ColorDto> getColors() {

        return colorMapper.mapToColorDtoList(colorRepository.findAll());

    }
}
