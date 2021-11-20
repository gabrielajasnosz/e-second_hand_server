package com.esecondhand.esecondhand.service.serviceImpl;


import com.esecondhand.esecondhand.domain.dto.BrandDto;
import com.esecondhand.esecondhand.domain.dto.CategoryDto;
import com.esecondhand.esecondhand.domain.dto.ColorDto;
import com.esecondhand.esecondhand.domain.entity.Category;
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

    private SizeMapper sizeMapper;

    private BrandRepository brandRepository;

    private SizeRepository sizeRepository;

    private BrandMapper brandMapper;

    private ColorMapper colorMapper;

    private ColorRepository colorRepository;

    private CategoryRepository categoryRepository;


    public CategoryServiceImpl(CategoryMapper categoryMapper, BrandRepository brandRepository, SizeMapper sizeMapper, SizeRepository sizeRepository, BrandMapper brandMapper, ColorMapper colorMapper, ColorRepository colorRepository, CategoryRepository categoryRepository) {
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

        return categoryMapper.mapToCategoryDtoList(categories);

    }

    public List<BrandDto> getBrands() {

        return brandMapper.mapToBrandDtoList(brandRepository.findAll());

    }

    public List<ColorDto> getColors() {

        return colorMapper.mapToColorDtoList(colorRepository.findAll());

    }
}
