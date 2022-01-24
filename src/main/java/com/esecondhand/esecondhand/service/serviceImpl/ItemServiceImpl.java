package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.*;
import com.esecondhand.esecondhand.domain.entity.*;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.exception.InvalidImagesNumberException;
import com.esecondhand.esecondhand.exception.InvalidItemPropertiesException;
import com.esecondhand.esecondhand.exception.ObjectDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import com.esecondhand.esecondhand.service.ItemService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    private BrandRepository brandRepository;

    private ColorRepository colorRepository;

    private SizeRepository sizeRepository;

    private CategoryRepository categoryRepository;

    private UserRepository userRepository;

    private CommentRepository commentRepository;

    private ItemPictureRepository itemPictureRepository;

    private FollowerRepository followerRepository;

    private ItemMapper itemMapper;

    private Clock clock;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, BrandRepository brandRepository, ColorRepository colorRepository, SizeRepository sizeRepository, CategoryRepository categoryRepository, UserRepository userRepository, CommentRepository commentRepository, ItemPictureRepository itemPictureRepository, FollowerRepository followerRepository, Clock clock) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.brandRepository = brandRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.itemPictureRepository = itemPictureRepository;
        this.followerRepository = followerRepository;
        this.clock = clock;
    }


    public ItemDto saveItem(ItemEntryDto itemEntryDto) throws IOException, InvalidImagesNumberException, InvalidItemPropertiesException {
        if ((itemEntryDto.getMainImage() == null
                && (itemEntryDto.getImages() == null
                || itemEntryDto.getImages().length == 0))
                || (itemEntryDto.getImages() != null && itemEntryDto.getImages().length > 5)) {
            throw new InvalidImagesNumberException("Images number should be between 1-6");
        }

        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Brand brand = brandRepository.findByNameIgnoreCase(itemEntryDto.getBrand());
        if (brand == null) {
            brand = brandRepository.save(new Brand(null, itemEntryDto.getBrand()));
        }

        Item item = itemMapper.mapToItem(itemEntryDto);

        Category category = categoryRepository.findById(itemEntryDto.getCategoryId()).orElse(null);
        Color color = colorRepository.findById(itemEntryDto.getColorId()).orElse(null);
        Size size = sizeRepository.findById(itemEntryDto.getSizeId()).orElse(null);

        if (category == null || color == null || size == null) {
            throw new InvalidItemPropertiesException("New item properties are invalid");
        }

        item.setCategory(category);
        item.setColor(color);
        item.setSize(size);
        item.setUser(appUser.getUser());
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
        String SUB_DIR = item.getUser().getId().toString() + "/item-images/" + item.getId().toString() + "/";

        String FILE_LOCATION = SUB_DIR + item.getId().toString() + "-" + LocalDateTime.now(clock).getNano() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Path newFile = Paths.get(MAIN_DIR + FILE_LOCATION);
        Files.createDirectories(newFile.getParent());
        Files.write(newFile, file.getBytes());
        itemPictureRepository.save(new ItemPicture(null, item, LocalDateTime.now(), FILE_LOCATION, isMainPicture));
    }

    @Override
    public ItemDto getItem(Long itemId) throws ObjectDoesntExistsException {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = null;
        if (user instanceof AppUser) {
            appUser = (AppUser) user;
        }
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null || !item.getIsActive() || (item.getIsHidden() && appUser == null) || (item.getIsHidden() && !(appUser == null) && !item.getUser().getId().equals(appUser.getUser().getId()))) {
            throw new ObjectDoesntExistsException("Item for given id don't exist");
        }
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto editItem(EditItemDto editItemDto) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Item item = itemRepository.findById(editItemDto.getItemId()).orElse(null);
        if (item == null) {
            throw new ObjectDoesntExistsException("Item with provided id don't exist");
        }
        if (!appUser.getUser().getId().equals(item.getUser().getId())) {
            throw new ObjectDoesntBelongToUserException("You can edit only your item!");
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

    private List<Long> getCategoryIds(List<Long> resultList, Category category) {
        resultList.add(category.getId());
        if (category.getSubCategories().size() > 0) {
            for (Category subcategory : category.getSubCategories()) {
                getCategoryIds(resultList, subcategory);
            }
        }
        return resultList;
    }

    @Override
    public List<ItemPreviewDto> getItems(ItemListFiltersDto itemListFiltersDto) throws ParseException {

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = null;
        if (user instanceof AppUser) {
            appUser = (AppUser) user;
        }
        List<Long> followersIds = new ArrayList<>();
        if (appUser != null && itemListFiltersDto.isOnlyFollowedUsers()) {
            followersIds = followerRepository.getUserFollowedUsersIds(appUser.getUserId());
        }

        List<Long> result = new ArrayList<>();
        if (itemListFiltersDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(itemListFiltersDto.getCategoryId()).orElse(null);
            if (category != null) {
                result = getCategoryIds(new ArrayList<>(), category);
            }
        }

        List<Item> itemList = itemRepository.findItems(itemListFiltersDto, result, followersIds);

        Map<Long, Long> mainPictureIdByItemId = new HashMap<>();

        for (Item item : itemList) {
            mainPictureIdByItemId.put(item.getId(), itemPictureRepository.findMainImageIdByItemId(item.getId()));
        }

        List<ItemPreviewDto> itemPreviewDtoList = itemMapper.mapToPreviewList(itemList, mainPictureIdByItemId);


        return itemPreviewDtoList;
    }

    private Item verifyRequest(Long itemId) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            throw new ObjectDoesntExistsException("Item with provided id don't exist");
        }
        if (!appUser.getUser().getId().equals(item.getUser().getId())) {
            throw new ObjectDoesntBelongToUserException("You can edit only your item!");
        }
        return item;
    }


    @Override
    public void deleteItem(Long itemId) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException {
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
    public List<ItemPreviewDto> getHiddenItems() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Item> itemList = itemRepository.findAllByUserIdAndIsHiddenIsTrueAndIsActiveIsTrue(appUser.getUser().getId()).orElse(new ArrayList<>());

        Map<Long, Long> mainPictureIdByItemId = new HashMap<>();

        for (Item item : itemList) {
            mainPictureIdByItemId.put(item.getId(), itemPictureRepository.findMainImageIdByItemId(item.getId()));
        }

        List<ItemPreviewDto> itemPreviewDtoList = itemMapper.mapToPreviewList(itemList, mainPictureIdByItemId);
        return itemPreviewDtoList;
    }

    @Override
    public CountersDto getUserItemsCounters(Long userId) throws ObjectDoesntExistsException {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = null;
        if (user instanceof AppUser) {
            appUser = (AppUser) user;
        }
        if (userRepository.findById(userId).orElse(null) == null) {
            throw new ObjectDoesntExistsException("Item for given id don't exist");
        }
        CountersDto counters = new CountersDto();
        if (appUser != null && appUser.getUser().getId().equals(userId)) {
            counters.setHiddenItemsCounter(itemRepository.countByUserIdAndIsHiddenIsTrueAndIsActiveIsTrue(userId));
        }
        counters.setItemsCounter(itemRepository.countByUserIdAndIsHiddenIsFalseAndIsActiveIsTrue(userId));

        counters.setCommentsCounter(commentRepository.countAllByReceiverId(userId));

        counters.setFollowersCounter(followerRepository.countAllByFollowingId(userId));

        counters.setFollowingCounter(followerRepository.countAllByFollowerId(userId));

        return counters;
    }

    @Override
    public void manageItemVisibility(Long itemId, boolean status) throws ObjectDoesntExistsException, ObjectDoesntBelongToUserException {
        Item item = verifyRequest(itemId);
        item.setIsHidden(status);
        itemRepository.save(item);
    }

    @Override
    public List<ItemPreviewDto> getFollowedUsersItems(Long userId, int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);

        List<Long> followersIds = followerRepository.getUserFollowedUsersIds(userId);

        List<Item> items = itemRepository.findAllByUserIdInAndIsActiveIsTrueAndIsHiddenFalseOrderByCreationDateDesc(followersIds, pageRequest).orElse(null);

        List<ItemPreviewDto> followedUsersItems = new ArrayList<>();
        Map<Long, Long> mainPictureIdByItemId = new HashMap<>();

        if (items != null) {
            for (Item item : items) {
                mainPictureIdByItemId.put(item.getId(), itemPictureRepository.findMainImageIdByItemId(item.getId()));
            }
            followedUsersItems = itemMapper.mapToPreviewList(items, mainPictureIdByItemId);
        }

        return followedUsersItems;

    }

    @Override
    public void reportItem(ReportDto reportDto) {
        Item item = itemRepository.findById(reportDto.getItemId()).orElse(null);
        if (item != null) {
            item.setIsActive(false);
            itemRepository.save(item);
        }

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
