package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.*;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.mapper.ItemPictureMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.exception.InvalidImagesNumberException;
import com.esecondhand.esecondhand.exception.InvalidItemPropertiesException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemServiceImplTest {


    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ItemPictureMapper itemPictureMapper;

    @Mock
    private ItemPictureRepository itemPictureRepository;

    @Mock
    private ColorRepository colorRepository;

    @Mock
    private SizeRepository sizeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private FollowerRepository followerRepository;



    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
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

    @Test
    public void shouldSaveItem() throws IOException, InvalidImagesNumberException, InvalidItemPropertiesException {
        ItemEntryDto itemEntryDto = createCorrectEntryDto();
        when((AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(createAppUser());
        when(brandRepository.save(any(Brand.class))).thenReturn(new Brand(8L, "Test brand"));
        when(itemMapper.mapToItem(itemEntryDto)).thenCallRealMethod();
        when(categoryRepository.findById(itemEntryDto.getCategoryId())).thenReturn(Optional.of(createCorrectCategory()));
        when(sizeRepository.findById(itemEntryDto.getSizeId())).thenReturn(Optional.of(createCorrectSize()));
        when(colorRepository.findById(itemEntryDto.getColorId())).thenReturn(Optional.of(createCorrectColor()));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(itemMapper.mapToItemDto(any(Item.class))).thenCallRealMethod();

        ItemDto itemDto = itemService.saveItem(itemEntryDto);
        System.out.println(itemDto);


        assertTrue(true);
    }

    //
    @Test
    public void shouldThrowException_WhenCategoryDontExists() throws IOException, InvalidImagesNumberException, InvalidItemPropertiesException {
        ItemEntryDto itemEntryDto = createCorrectEntryDto();
        when((AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(createAppUser());
        when(brandRepository.save(any(Brand.class))).thenReturn(new Brand(8L, "Test brand"));
        when(itemMapper.mapToItem(itemEntryDto)).thenCallRealMethod();
        when(categoryRepository.findById(itemEntryDto.getCategoryId())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(InvalidItemPropertiesException.class, () -> {
            itemService.saveItem(itemEntryDto);
        });

        String expectedMessage = "New item properties are invalid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
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
    private Category createCorrectCategory() {
        Category category = new Category();
        category.setId(2L);
        category.setName("Test category");
        return category;
    }
    private Size createCorrectSize() {
        Size size = new Size();
        size.setId(3L);
        size.setProductType(1L);
        size.setName("Test category");
        return size;
    }
    private Color createCorrectColor() {
        Color color = new Color();
        color.setId(4L);
        color.setName("Test color");
        return color;
    }

    private AppUser createAppUser() {
        User user = new User();
        user.setRole("USER");
        user.setPassword("test");
        user.setGender(Gender.valueOf("MAN"));
        user.setZipCode("33-100");
        user.setEnabled(true);
        user.setCreationDate(LocalDateTime.now());
        user.setDisplayName("test testowy");
        user.setEmail("test@test.com");
        user.setId(90L);
        return new AppUser(user);

    }

}
