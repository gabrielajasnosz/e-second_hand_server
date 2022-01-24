package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.dto.SizeDto;

import java.util.List;
import java.util.Map;

public interface SizeService {
    Map<Long, List<SizeDto>> getSizes();
    List<SizeDto> getSizesUngrouped();
}
