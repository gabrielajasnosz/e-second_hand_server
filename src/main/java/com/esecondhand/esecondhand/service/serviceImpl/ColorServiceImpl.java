package com.esecondhand.esecondhand.service.serviceImpl;


import com.esecondhand.esecondhand.domain.dto.ColorDto;
import com.esecondhand.esecondhand.domain.mapper.ColorMapper;
import com.esecondhand.esecondhand.domain.repository.ColorRepository;
import com.esecondhand.esecondhand.service.ColorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {

    private ColorMapper colorMapper;

    private ColorRepository colorRepository;

    public ColorServiceImpl(ColorMapper colorMapper, ColorRepository colorRepository) {
        this.colorMapper = colorMapper;
        this.colorRepository = colorRepository;
    }

    @Override
    public List<ColorDto> getColors() {

        return colorMapper.mapToColorDtoList(colorRepository.findAll());

    }
}
