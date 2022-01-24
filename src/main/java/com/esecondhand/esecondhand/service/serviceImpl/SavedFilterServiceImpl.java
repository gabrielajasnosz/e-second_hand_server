package com.esecondhand.esecondhand.service.serviceImpl;


import com.esecondhand.esecondhand.domain.dto.SavedFilterDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterEntryDto;
import com.esecondhand.esecondhand.domain.dto.SavedFilterPreviewDto;
import com.esecondhand.esecondhand.domain.entity.AppUser;
import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.entity.SavedFilter;
import com.esecondhand.esecondhand.domain.mapper.SavedFilterMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.exception.ObjectDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import com.esecondhand.esecondhand.service.SavedFilterService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedFilterServiceImpl implements SavedFilterService {

    private BrandRepository brandRepository;

    private ColorRepository colorRepository;

    private SizeRepository sizeRepository;

    private CategoryRepository categoryRepository;

    private UserRepository userRepository;

    private SavedFilterRepository savedFilterRepository;

    private SavedFilterMapper savedFilterMapper;

    public SavedFilterServiceImpl(BrandRepository brandRepository, ColorRepository colorRepository, SizeRepository sizeRepository, CategoryRepository categoryRepository, UserRepository userRepository, SavedFilterRepository savedFilterRepository, SavedFilterMapper savedFilterMapper) {
        this.brandRepository = brandRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.savedFilterRepository = savedFilterRepository;
        this.savedFilterMapper = savedFilterMapper;
    }

    @Override
    public SavedFilter saveFilters(SavedFilterEntryDto savedFilterEntryDto) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        SavedFilter savedFilter = new SavedFilter();
        if(savedFilterEntryDto.getBrandId() != null){
            savedFilter.setBrand(brandRepository.findById(savedFilterEntryDto.getBrandId()).orElse(null));
        }
        if(savedFilterEntryDto.getCategoryId() != null){
            savedFilter.setCategory(categoryRepository.findById(savedFilterEntryDto.getCategoryId()).orElse(null));
        }
        if(savedFilterEntryDto.getColorId() != null){
            savedFilter.setColor(colorRepository.findById(savedFilterEntryDto.getColorId()).orElse(null));
        }
        if(savedFilterEntryDto.getSizeId() != null){
            savedFilter.setSize(sizeRepository.findById(savedFilterEntryDto.getSizeId()).orElse(null));
        }

        savedFilter.setUser(userRepository.findById(appUser.getUser().getId()).orElse(null));

        if(savedFilterEntryDto.getGender() != null){
            savedFilter.setGender(Gender.valueOf(savedFilterEntryDto.getGender().toUpperCase()));
        }


        savedFilter.setName(savedFilterEntryDto.getName());

        savedFilter.setMinPrice(savedFilterEntryDto.getMinPrice());
        savedFilter.setMaxPrice(savedFilterEntryDto.getMaxPrice());
        savedFilter.setSortingColumn(savedFilterEntryDto.getSortingColumn());
        savedFilter.setSortingOrder(savedFilterEntryDto.getSortingOrder());

        return savedFilterRepository.save(savedFilter);

    }

    @Override
    public List<SavedFilterPreviewDto> getSavedFilters() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<SavedFilter> savedFilters = savedFilterRepository.findAllByUserId(appUser.getUser().getId());
        return savedFilterMapper.mapToSavedFilterDtoList(savedFilters);
    }

    @Override
    public void deleteFilter(Long id) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        SavedFilter savedFilter = savedFilterRepository.findById(id).orElse(null);
        if(savedFilter == null){
            throw new ObjectDoesntExistsException("Provided id is incorrect.");
        }
        if(!savedFilter.getUser().getId().equals(appUser.getUser().getId())){
            throw new ObjectDoesntBelongToUserException("Saved filter with provided does not belong to user.");
        }
        savedFilterRepository.deleteById(id);
    }

    @Override
    public SavedFilterDto getSavedFilter(Long savedFilterId) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        SavedFilter savedFilter = savedFilterRepository.findById(savedFilterId).orElse(null);
        if(savedFilter == null){
            throw new ObjectDoesntExistsException("Provided id is incorrect.");
        }
        if(!savedFilter.getUser().getId().equals(appUser.getUser().getId())){
            throw new ObjectDoesntBelongToUserException("Saved filter with provided does not belong to user.");
        }


        return savedFilterMapper.mapToSavedFilterDto(savedFilter);
    }
}
