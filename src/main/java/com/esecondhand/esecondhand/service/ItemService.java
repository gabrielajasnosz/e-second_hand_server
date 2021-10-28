package com.esecondhand.esecondhand.service;

import com.esecondhand.esecondhand.domain.AppUser;
import com.esecondhand.esecondhand.domain.Brand;
import com.esecondhand.esecondhand.domain.Gender;
import com.esecondhand.esecondhand.domain.Item;
import com.esecondhand.esecondhand.dto.ImageDto;
import com.esecondhand.esecondhand.dto.ItemDto;
import com.esecondhand.esecondhand.dto.ItemEntryDto;
import com.esecondhand.esecondhand.mapper.ItemMapper;
import com.esecondhand.esecondhand.repository.BrandRepository;
import com.esecondhand.esecondhand.repository.ItemRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service
public class ItemService {

    private ItemRepository itemRepository;

    private BrandRepository brandRepository;

    private ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, BrandRepository brandRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.brandRepository = brandRepository;
        this.itemMapper = itemMapper;
    }

    public Item saveItem(ItemEntryDto itemEntryDto) throws IOException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Brand brand = brandRepository.findByNameIgnoreCase(itemEntryDto.getBrand());
        if (brand == null) {
            brand = brandRepository.save(new Brand(null, itemEntryDto.getBrand()));
        }
        saveUploadedFile(itemEntryDto.getFiles());
        ItemDto item = new ItemDto(itemEntryDto.getName(), appUser.getUserId(), itemEntryDto.getDescription(), itemEntryDto.getCategoryId(), brand.getId(), itemEntryDto.getColorId(), itemEntryDto.getPrice(), itemEntryDto.getSizeId(), Gender.valueOf(itemEntryDto.getSex().toUpperCase()), new Date());
        return itemRepository.save(itemMapper.mapToItem(item));
    }

    private void saveUploadedFile(MultipartFile[] files) throws IOException {

        for (MultipartFile file : files) {
            File fileToSave = new File("C:\\Users\\Gabriela\\IdeaProjects\\e-second_hand_server\\src\\main\\resources\\" + file.getOriginalFilename());
            file.transferTo(fileToSave);
        }

    }

}
