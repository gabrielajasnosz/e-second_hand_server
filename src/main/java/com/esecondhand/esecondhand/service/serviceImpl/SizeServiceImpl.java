package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.SizeDto;
import com.esecondhand.esecondhand.domain.mapper.SizeMapper;
import com.esecondhand.esecondhand.domain.repository.SizeRepository;
import com.esecondhand.esecondhand.service.SizeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SizeServiceImpl implements SizeService {

    private SizeRepository sizeRepository;

    private SizeMapper sizeMapper;


    public SizeServiceImpl(SizeRepository sizeRepository, SizeMapper sizeMapper) {
        this.sizeRepository = sizeRepository;
        this.sizeMapper = sizeMapper;
    }

    @Override
    public Map<Long, List<SizeDto>> getSizes() {
        List<SizeDto> sizes = sizeMapper.mapToSizeDtoList(sizeRepository.findAll());

        return sizes.stream().collect(Collectors.groupingBy(SizeDto::getProductType));
    }

    @Override
    public List<SizeDto> getSizesUngrouped() {
        return sizeMapper.mapToSizeDtoList(sizeRepository.findAll());
    }
}
