package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.EditItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.*;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.exception.ItemDontExistsException;
import com.esecondhand.esecondhand.service.ItemService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    private BrandRepository brandRepository;

    private ColorRepository colorRepository;

    private SizeRepository sizeRepository;

    private CategoryRepository categoryRepository;

    private UserRepository userRepository;

    private ItemPictureRepository itemPictureRepository;

    private ItemMapper itemMapper;


    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, BrandRepository brandRepository, ColorRepository colorRepository, SizeRepository sizeRepository, CategoryRepository categoryRepository, UserRepository userRepository, ItemPictureRepository itemPictureRepository, ItemMapper itemMapper1) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.brandRepository = brandRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.itemPictureRepository = itemPictureRepository;
        this.itemMapper = itemMapper1;
    }


    public Long saveItem(ItemEntryDto itemEntryDto) throws IOException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Brand brand = brandRepository.findByNameIgnoreCase(itemEntryDto.getBrand());
        if (brand == null) {
            brand = brandRepository.save(new Brand(null, itemEntryDto.getBrand()));
        }

        Item item = itemMapper.mapToItem(itemEntryDto);
        item.setCategory(categoryRepository.findById(itemEntryDto.getCategoryId()).orElse(null));
        item.setColor(colorRepository.findById(itemEntryDto.getColorId()).orElse(null));
        item.setSize(sizeRepository.findById(itemEntryDto.getSizeId()).orElse(null));
        item.setUser(userRepository.findById(appUser.getUser().getId()).orElse(null));
        item.setBrand(brand);

        Item itemSaved = itemRepository.save(item);
        saveUploadedFile(itemEntryDto.getMainImage(), itemSaved, true);
        if (itemEntryDto.getImages() != null) {
            for (MultipartFile file : itemEntryDto.getImages()) {
                saveUploadedFile(file, itemSaved, false);
            }
        }
        return itemSaved.getId();
    }

    private void saveUploadedFile(MultipartFile file, Item item, boolean isMainPicture) throws IOException {

        String MAIN_DIR = "src/main/resources/images/";
        String SUB_DIR = item.getUser().getId().toString() + "/" + item.getId().toString() + "/";

        String FILE_LOCATION = SUB_DIR + item.getId().toString() + "-" + new Date().getTime() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Path newFile = Paths.get(MAIN_DIR + FILE_LOCATION);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, file.getBytes());
        itemPictureRepository.save(new ItemPicture(null, item, new Date(), FILE_LOCATION, isMainPicture));


    }

    public ItemDto getItem(Long itemId) throws ItemDontExistsException {
        Item item = itemRepository.findById(itemId).orElse(null);
        if(item == null){
            throw new ItemDontExistsException("Item for given id don't exist");
        }
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public Item editItem(EditItemDto editItemDto) throws ItemDontExistsException {
        Item item = itemRepository.findById(editItemDto.getItemId()).orElse(null);
        if(item == null){
            throw new ItemDontExistsException("Item with provided id don't exist");
        }
        Brand brand = brandRepository.findByNameIgnoreCase(editItemDto.getBrand());
        if(brand == null){
            brand = brandRepository.save(new Brand(null, editItemDto.getBrand()));
        }
        Color color = colorRepository.findByNameIgnoreCase(editItemDto.getColor());
        Size size = sizeRepository.findByNameIgnoreCase(editItemDto.getSize());

        Category category = categoryRepository.findByNameIgnoreCase(editItemDto.getCategory());

        item.setBrand(brand);
        item.setCategory(category);
        item.setColor(color);
        item.setDescription(editItemDto.getDescription());
        item.setName(editItemDto.getName());
        item.setSize(size);
        item.setGender(Gender.valueOf(editItemDto.getGender()));
        item.setPrice(editItemDto.getPrice());

        return itemRepository.save(item);

    }

    public FileSystemResource find(Long imageId) {
        ItemPicture image = itemPictureRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return findInFileSystem(image.getFileUrl());
    }

    private FileSystemResource findInFileSystem(String location) {
        String MAIN_DIR = "src/main/resources/images/";
        try {
            return new FileSystemResource(Paths.get(MAIN_DIR + location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
