package com.esecondhand.esecondhand.service.serviceImpl;

import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.AppUser;
import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.mapper.ItemMapper;
import com.esecondhand.esecondhand.domain.mapper.ItemPictureMapper;
import com.esecondhand.esecondhand.domain.repository.*;
import com.esecondhand.esecondhand.exception.InvalidItemPropertiesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ItemServiceImplUnitTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BrandRepository brandRepository;

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
    private ItemPictureRepository itemPictureRepository;
    @Mock
    private FollowerRepository followerRepository;
    @Mock
    private ItemMapper itemMapper;


    @InjectMocks
    private ItemServiceImpl itemService;

    private ItemPictureMapper itemPictureMapper;

    private final static LocalDate LOCAL_DATE = LocalDate.of(2022, 01, 13);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        itemPictureMapper = mock(ItemPictureMapper.class);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when((AppUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).thenReturn(new AppUser(createAppUser()));
    }

    @Test
    public void shouldThrowException_WhenCategoryDontExists() {
        //given
        ItemEntryDto itemEntryDto = createCorrectEntryDto();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        String expectedMessage = "New item properties are invalid";

        //when
        Exception exception = assertThrows(InvalidItemPropertiesException.class, () -> {
            itemService.saveItem(itemEntryDto);
        });

        //then
        assertTrue(exception.getMessage().contains(expectedMessage));
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
}
