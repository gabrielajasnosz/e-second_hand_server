package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.*;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.mapper.ItemPictureMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.exception.InvalidImagesNumberException;
import com.esecondhand.esecondhand.exception.InvalidItemPropertiesException;
import com.esecondhand.esecondhand.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceImplTest {


    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ItemMapper itemMapper;

    @MockBean
    private ItemPictureRepository itemPictureRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private FollowerRepository followerRepository;

    @MockBean
    private Clock clock;
    private Clock fixedClock;

    private final static LocalDate LOCAL_DATE = LocalDate.of(1989, 01, 13);


    User user;
    Item item;
    Brand brand;
    Color color;
    Size size;
    Category category;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        //itemPictureMapper = mock(ItemPictureMapper.class);
        user = userRepository.save(createAppUser());
        item = createItem();

        brand = brandRepository.save(createCorrectBrand());
        color = colorRepository.save(createCorrectColor());
        category = categoryRepository.save(createCorrectCategory());
        size = sizeRepository.save(createCorrectSize());

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(new AppUser(user));

        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

    }

    @Test
    public void shouldSaveItemWhenCorrectEntryValues() throws IOException, InvalidItemPropertiesException, InvalidImagesNumberException {

        ItemEntryDto itemEntryDto = createCorrectEntryDto(brand, color, category, size);

        ItemDto itemDto = itemService.saveItem(itemEntryDto);

        assertEquals(itemDto.getUserId(), user.getId());
        assertEquals(itemDto.getBrand(), brand.getName());
        assertEquals(itemDto.getIsHidden(), false);
        assertEquals(itemDto.getIsActive(), true);
        assertEquals(itemDto.getSizeId(), size.getId());
        assertEquals(itemDto.getName(), itemEntryDto.getName());
        assertEquals(itemDto.getCategoryId(), category.getId());

    }


    @Test
    public void shouldThrowException_WhenIncorrectImagesNumber() {
        ItemEntryDto itemEntryDto = new ItemEntryDto();
        Exception exception = assertThrows(InvalidImagesNumberException.class, () -> {
            itemService.saveItem(itemEntryDto);
        });

        String expectedMessage = "Images number should be between 1-6";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    //
    @Test
    public void shouldThrowException_WhenCategoryDontExists() {
        ItemEntryDto itemEntryDto = createCorrectEntryDto();

        Exception exception = assertThrows(InvalidItemPropertiesException.class, () -> {
            itemService.saveItem(itemEntryDto);
        });

        String expectedMessage = "New item properties are invalid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenUploadedFileSavedThenCheckFileExistenceAndPath() throws IOException, InvalidItemPropertiesException, InvalidImagesNumberException {

        ItemEntryDto itemEntryDto = createEntryDtoWithOneImage(brand, color, category, size );

        ItemDto itemDto = itemService.saveItem(itemEntryDto);

        String MAIN_DIR = "src/main/resources/images/";
        String SUB_DIR = itemDto.getUserId() + "/item-images/" + itemDto.getId() + "/";
        String FILE_NAME = itemDto.getId() + "-" + LocalDateTime.now(clock).getNano() + "." + "txt";


        Path path = Paths.get(MAIN_DIR + SUB_DIR + FILE_NAME);
        assertTrue(Files.exists(path));
    }


    private ItemEntryDto createCorrectEntryDto() {
        MockMultipartFile firstFile = new MockMultipartFile("firstImage", new byte[1]);
        MockMultipartFile secondFile = new MockMultipartFile("secondImage", new byte[1]);
        MockMultipartFile mainImage = new MockMultipartFile("mainImage", new byte[1]);
        MultipartFile[] files = {firstFile, secondFile};
        ItemEntryDto itemEntryDto = new ItemEntryDto();
        itemEntryDto.setBrand("Test brand");
        itemEntryDto.setColorId(4L);
        itemEntryDto.setImages(files);
        itemEntryDto.setSex("WOMAN");
        itemEntryDto.setCategoryId(2L);
        itemEntryDto.setName("Test name");
        itemEntryDto.setDescription("Test description");
        itemEntryDto.setMainImage(mainImage);
        itemEntryDto.setSizeId(3L);
        itemEntryDto.setPrice(90.0);
        return itemEntryDto;
    }

    private ItemEntryDto createEntryDtoWithOneImage(Brand brand, Color color, Category category, Size size) {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        ItemEntryDto itemEntryDto = new ItemEntryDto();
        itemEntryDto.setBrand("brand");
        itemEntryDto.setColorId(color.getId());
        itemEntryDto.setSex("WOMAN");
        itemEntryDto.setCategoryId(category.getId());
        itemEntryDto.setName("Test name");
        itemEntryDto.setDescription("Test description");
        itemEntryDto.setMainImage(firstFile);
        itemEntryDto.setSizeId(size.getId());
        itemEntryDto.setPrice(90.0);
        return itemEntryDto;
    }

    private ItemEntryDto createCorrectEntryDto(Brand brand, Color color, Category category, Size size) {
        MockMultipartFile firstFile = new MockMultipartFile("firstImage", new byte[1]);
        MockMultipartFile secondFile = new MockMultipartFile("secondImage", new byte[1]);
        MockMultipartFile mainImage = new MockMultipartFile("mainImage", new byte[1]);
        MultipartFile[] files = {firstFile, secondFile};
        ItemEntryDto itemEntryDto = new ItemEntryDto();
        itemEntryDto.setBrand("brand");
        itemEntryDto.setColorId(color.getId());
        itemEntryDto.setImages(files);
        itemEntryDto.setSex("WOMAN");
        itemEntryDto.setCategoryId(category.getId());
        itemEntryDto.setName("Test name");
        itemEntryDto.setDescription("Test description");
        itemEntryDto.setMainImage(mainImage);
        itemEntryDto.setSizeId(size.getId());
        itemEntryDto.setPrice(90.0);
        return itemEntryDto;
    }

    private Category createCorrectCategory() {
        Category category = new Category();
        category.setName("Test category");
        return category;
    }

    private Size createCorrectSize() {
        Size size = new Size();
        size.setProductType(1L);
        size.setName("Test category");
        return size;
    }

    private Brand createCorrectBrand() {
        Brand brand = new Brand();
        brand.setName("brand");
        return brand;
    }

    private Color createCorrectColor() {
        Color color = new Color();
        color.setName("Test color");
        return color;
    }

    private User createAppUser() {
        User user = new User();
        user.setRole("USER");
        user.setPassword("test");
        user.setGender(Gender.valueOf("MAN"));
        user.setZipCode("33-100");
        user.setEnabled(true);
        user.setCreationDate(LocalDateTime.now());
        user.setDisplayName("test testowy");
        user.setEmail("test@test.com");
        return user;

    }


    private Item createItem() {
        Item item = new Item();
        item.setId(150L);
        item.setName("name");
        item.setDescription("desc");
        item.setCreationDate(LocalDateTime.now());
        item.setPrice(5.0D);
        item.setGender(Gender.valueOf("MAN"));
        item.setUser(user);
        return item;
    }


}
