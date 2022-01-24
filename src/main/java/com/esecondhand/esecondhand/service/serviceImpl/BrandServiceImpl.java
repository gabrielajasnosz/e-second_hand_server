package com.esecondhand.esecondhand.service.serviceImpl;


import com.esecondhand.esecondhand.domain.dto.BrandDto;
import com.esecondhand.esecondhand.domain.mapper.BrandMapper;
import com.esecondhand.esecondhand.domain.repository.BrandRepository;
import com.esecondhand.esecondhand.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private BrandRepository brandRepository;

    private BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public List<BrandDto> getBrands() {

        return brandMapper.mapToBrandDtoList(brandRepository.findAll());

    }
}
