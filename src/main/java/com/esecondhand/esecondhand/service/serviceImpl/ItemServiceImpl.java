package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.entity.AppUser;
import com.esecondhand.esecondhand.domain.entity.Brand;
import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.ItemPicture;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.service.ItemService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.regex.Pattern;


@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    private ItemMapper itemMapper;

    private BrandRepository brandRepository;

    private ColorRepository colorRepository;

    private SizeRepository sizeRepository;

    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    private ItemPictureRepository itemPictureRepository;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, BrandRepository brandRepository, ColorRepository colorRepository, SizeRepository sizeRepository, CategoryRepository categoryRepository, UserRepository userRepository, ItemPictureRepository itemPictureRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.brandRepository = brandRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.itemPictureRepository = itemPictureRepository;
    }

    public Item saveItem(ItemEntryDto itemEntryDto) throws IOException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Brand brand = brandRepository.findByNameIgnoreCase(itemEntryDto.getBrand());
        if (brand == null){
            brand = brandRepository.save(new Brand(null, itemEntryDto.getBrand()));
        }

        Item item = itemMapper.mapToItem(itemEntryDto);
        item.setCategory(categoryRepository.getById(itemEntryDto.getCategoryId()));
        item.setColor(colorRepository.getById(itemEntryDto.getColorId()));
        item.setSize(sizeRepository.getById(itemEntryDto.getSizeId()));
        item.setUser(userRepository.getById(appUser.getUser().getId()));
        item.setBrand(brand);

        Item itemSaved = itemRepository.save(item);
        saveUploadedFile(itemEntryDto.getFiles(), itemSaved);

        return itemSaved;
    }

    private void saveUploadedFile(MultipartFile[] files, Item item) throws IOException {


        for (MultipartFile file : files) {
            Path newFile = Paths.get(this.getClass().getResource("/").getPath() + new Date().getTime() + "-" + file.getOriginalFilename());
            Files.createDirectories(newFile.getParent());

            Files.write(newFile, file.getBytes());
            itemPictureRepository.save(new ItemPicture(null, item, new Date(), newFile.toAbsolutePath().toString()));
        }

    }

    private static final Pattern WINDOWS_SLASH_DRIVE_PREFIX = Pattern.compile("\\/[A-Z]:\\/.*");
    private static final Pattern WINDOWS_DRIVE_PREFIX = Pattern.compile("^[A-Z]:\\/.*");

    public static String formOf(String path) {
        if (WINDOWS_SLASH_DRIVE_PREFIX.matcher(path).matches()) {
            return path.substring(1).replace("/","\\");
        } else if (WINDOWS_DRIVE_PREFIX.matcher(path).matches()) {
            return path.replace("/","\\");
        }
        return path;
    }

    public FileSystemResource find(Long imageId) {
        ItemPicture image = itemPictureRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return findInFileSystem(image.getFileUrl());
    }

     private FileSystemResource findInFileSystem(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
