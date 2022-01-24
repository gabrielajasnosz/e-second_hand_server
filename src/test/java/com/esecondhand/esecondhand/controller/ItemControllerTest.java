package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemListFiltersDto;
import com.esecondhand.esecondhand.domain.dto.ItemPictureDto;
import com.esecondhand.esecondhand.domain.dto.ItemPreviewDto;
import com.esecondhand.esecondhand.domain.entity.Gender;
import com.esecondhand.esecondhand.domain.repository.ItemRepository;
import com.esecondhand.esecondhand.exception.InvalidImagesNumberException;
import com.esecondhand.esecondhand.security.JwtAuthenticationEntryPoint;
import com.esecondhand.esecondhand.security.JwtTokenUtil;
import com.esecondhand.esecondhand.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.IOUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    @InjectMocks
    ItemController itemController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void returnHttpStatusCreatedWhenSavingItem() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("firstImage", new byte[1]);
        MockMultipartFile secondFile = new MockMultipartFile("secondImage", new byte[1]);
        when(itemService.saveItem(any())).thenReturn(createNewItemDto());
        mockMvc.perform(multipart("/items")
                .file("images", firstFile.getBytes())
                .file("mainImage", secondFile.getBytes())
                .param("name", "name test")
                .param("price", String.valueOf(90.0))
                .param("sex", "woman")
                .param("categoryId", "8")
                .param("sizeId", "5")
                .param("brand", "Bershka")
                .param("colorId", "3"))
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.colorId", Is.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand", Is.is("Bershka")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryGender", Is.is("UNDEFINED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", Is.is(90.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId", Is.is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("name test")))
                .andDo(print());
    }


    @Test
    public void returnHttpStatusBadRequestWhenIncorrectData() throws Exception {
        when(itemService.saveItem(any())).thenThrow(InvalidImagesNumberException.class);
        mockMvc.perform(multipart("/items")
                .param("name", "testname")
                .param("price", String.valueOf(90))
                .param("sex", "WOMAN")
                .param("categoryId", "4")
                .param("sizeId", "8")
                .param("brand", "Zara")
                .param("colorId", "2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void assertThatFileDownloadWorks() throws Exception {
        FileSystemResource image = new FileSystemResource("src/test/resources/images/test-1234.png");
        when(itemService.find(Mockito.anyLong())).thenReturn(image);
        mockMvc.perform(get("/items/image/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andExpect(MockMvcResultMatchers.content().bytes(IOUtils.toByteArray(image.getURL())));
    }

    public static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        return ow.writeValueAsString(obj);

    }

    @Test
    public void setDefaultFilterValuesWhenEmptyObject() throws Exception {
        ItemListFiltersDto itemListFiltersDto = new ItemListFiltersDto();
        final ArgumentCaptor<ItemListFiltersDto> captor = ArgumentCaptor.forClass(ItemListFiltersDto.class);
        when(itemService.getItems(any(ItemListFiltersDto.class))).thenReturn(new ArrayList<>());
        mockMvc.perform(post("/items/list")
                .content(asJsonString(itemListFiltersDto))
                .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(itemService).getItems(captor.capture());

        assertEquals(10, captor.getValue().getPageSize());
        assertEquals("DESC", captor.getValue().getSortingOrder());
        assertEquals("creationDate", captor.getValue().getSortingColumn());
    }

    @Test
    public void returnCorrectNextItemValuesWhenThereIsNextPage() throws Exception {
        ItemListFiltersDto itemListFiltersDto = new ItemListFiltersDto();
        itemListFiltersDto.setPageSize(2);
        itemListFiltersDto.setSortingColumn("price");
        when(itemService.getItems(any(ItemListFiltersDto.class))).thenReturn(createListOfThreeItemPreviewDto());
        mockMvc.perform(post("/items/list")
                .content(asJsonString(itemListFiltersDto))
                .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nextItemId", Is.is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nextItemValue", Is.is(70.0)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    private List<ItemPreviewDto> createListOfThreeItemPreviewDto() {
        List<ItemPreviewDto> itemPreviewDtoList = new ArrayList<>();
        itemPreviewDtoList.add(new ItemPreviewDto(2L,"first name", 4L, "test display name", 89.0, LocalDateTime.now(), 9L));
        itemPreviewDtoList.add(new ItemPreviewDto(3L,"second name", 4L, "test display name", 10.0, LocalDateTime.now(), 2L));
        itemPreviewDtoList.add(new ItemPreviewDto(8L,"third name", 4L, "test display name", 70.0, LocalDateTime.now(), 1L));

        return itemPreviewDtoList;
    }

    private ItemDto createNewItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setIsHidden(false);
        itemDto.setIsActive(true);
        itemDto.setProductType(1L);
        itemDto.setItemPictures(Arrays.asList(new ItemPictureDto(5L, "url1", true)));
        itemDto.setGender(Gender.valueOf("WOMAN"));
        itemDto.setBrand("Bershka");
        itemDto.setBrandId(2L);
        itemDto.setSize("L");
        itemDto.setSizeId(5L);
        itemDto.setPrice(90.0);
        itemDto.setColorId(3L);
        itemDto.setColor("PINK");
        itemDto.setUserDisplayName("test test");
        itemDto.setId(9L);
        itemDto.setCategoryId(8L);
        itemDto.setCategory("category test");
        itemDto.setCreationDate(LocalDateTime.now());
        itemDto.setCategoryGender(Gender.valueOf("UNDEFINED"));
        itemDto.setName("name test");
        itemDto.setUserId(2L);

        return itemDto;
    }


}
