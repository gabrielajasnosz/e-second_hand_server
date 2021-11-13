package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.*;
import com.esecondhand.esecondhand.domain.entity.*;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.exception.ItemDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ItemDoesntExistsException;
import com.esecondhand.esecondhand.service.ItemService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ItemServiceImpl implements ItemService {

    @PersistenceContext
    private EntityManager entityManager;

    private ItemRepository itemRepository;

    private BrandRepository brandRepository;

    private ColorRepository colorRepository;

    private SizeRepository sizeRepository;

    private CategoryRepository categoryRepository;

    private UserRepository userRepository;

    private ItemPictureRepository itemPictureRepository;

    private ItemMapper itemMapper;

    private final Long DEFAULT_PAGE_SIZE = 20L;

    private final String DEFAULT_SORTING_ORDER = "DESC";

    private final String DEFAULT_SORTING_COLUMN = "creationDate";


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


    public ItemDto saveItem(ItemEntryDto itemEntryDto) throws IOException {
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
        item.setIsActive(true);
        item.setIsHidden(false);
        item.setBrand(brand);

        Item itemSaved = itemRepository.save(item);
        saveUploadedFile(itemEntryDto.getMainImage(), itemSaved, true);
        if (itemEntryDto.getImages() != null) {
            for (MultipartFile file : itemEntryDto.getImages()) {
                saveUploadedFile(file, itemSaved, false);
            }
        }
        return itemMapper.mapToItemDto(itemSaved);
    }

    private void saveUploadedFile(MultipartFile file, Item item, boolean isMainPicture) throws IOException {

        String MAIN_DIR = "src/main/resources/images/";
        String SUB_DIR = item.getUser().getId().toString() + "/" + item.getId().toString() + "/";

        String FILE_LOCATION = SUB_DIR + item.getId().toString() + "-" + new Date().getTime() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Path newFile = Paths.get(MAIN_DIR + FILE_LOCATION);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, file.getBytes());
        itemPictureRepository.save(new ItemPicture(null, item, LocalDateTime.now(), FILE_LOCATION, isMainPicture));


    }

    @Override
    public ItemDto getItem(Long itemId) throws ItemDoesntExistsException {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = null;
        if (user instanceof AppUser) {
            appUser = (AppUser) user;
        }

        System.out.println(appUser);
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null || !item.getIsActive() || (item.getIsHidden() && appUser == null) || (item.getIsHidden() && !(appUser == null) && !item.getUser().getId().equals(appUser.getUser().getId()))) {
            throw new ItemDoesntExistsException("Item for given id don't exist");
        }
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto editItem(EditItemDto editItemDto) throws ItemDoesntExistsException, ItemDoesntBelongToUserException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Item item = itemRepository.findById(editItemDto.getItemId()).orElse(null);
        if (item == null) {
            throw new ItemDoesntExistsException("Item with provided id don't exist");
        }
        if (!appUser.getUser().getId().equals(item.getUser().getId())) {
            throw new ItemDoesntBelongToUserException("You can edit only your item!");
        }
        Brand brand = brandRepository.findByNameIgnoreCase(editItemDto.getBrand());
        if (brand == null) {
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

        Item savedItem = itemRepository.save(item);
        return itemMapper.mapToItemDto(savedItem);
    }

    private List<Long> getCategoryIds(List<Long> resultList, Category category){
        resultList.add(category.getId());
        if(category.getSubCategories().size()>0){
            for(Category subcategory: category.getSubCategories()){
                getCategoryIds(resultList,subcategory);
            }
        }
        return resultList;
    }

    @Override
    public List<ItemPreviewDto> getItems(ItemListFiltersDto itemListFiltersDto) throws ParseException {
        List<Long> result = new ArrayList<>();
        if(itemListFiltersDto.getCategoryId() != null){
            Category category = categoryRepository.findById(itemListFiltersDto.getCategoryId()).orElse(null);
            if(category != null){
                result = getCategoryIds(new ArrayList<>(),category);
            }
        }
       List<Item> itemList = itemRepository.findItems(itemListFiltersDto, result);

       Map<Long, Long> mainPictureIdByItemId = new HashMap<>();

       for(Item item : itemList){
           mainPictureIdByItemId.put(item.getId(),itemPictureRepository.findMainImageIdByItemId(item.getId()));
       }

       List<ItemPreviewDto> itemPreviewDtoList = itemMapper.mapToPreviewList(itemList,mainPictureIdByItemId);


       return itemPreviewDtoList;
    }

    private Item verifyRequest(Long itemId) throws ItemDoesntExistsException, ItemDoesntBelongToUserException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            throw new ItemDoesntExistsException("Item with provided id don't exist");
        }
        if (!appUser.getUser().getId().equals(item.getUser().getId())) {
            throw new ItemDoesntBelongToUserException("You can edit only your item!");
        }
        return item;
    }


    @Override
    public void deleteItem(Long itemId) throws ItemDoesntExistsException, ItemDoesntBelongToUserException {
        Item item = verifyRequest(itemId);
        item.setIsActive(false);
        itemRepository.save(item);
    }

    @Override
    public PriceExtremeValuesDto getPriceExtremeValues() {
        PriceExtremeValuesDto priceExtremeValuesDto = new PriceExtremeValuesDto();
        priceExtremeValuesDto.setMaxPrice(itemRepository.findMaxPrice());
        priceExtremeValuesDto.setMinPrice(itemRepository.findMinPrice());

        return priceExtremeValuesDto;

    }

    @Override
    public void manageItemVisibility(Long itemId, boolean status) throws ItemDoesntExistsException, ItemDoesntBelongToUserException {
        Item item = verifyRequest(itemId);
        item.setIsHidden(status);
        itemRepository.save(item);
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
