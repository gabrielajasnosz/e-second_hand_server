package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.ItemDto;
import com.esecondhand.esecondhand.domain.dto.ItemEntryDto;
import com.esecondhand.esecondhand.domain.entity.*;
import com.esecondhand.esecondhand.exception.InvalidImagesNumberException;
import com.esecondhand.esecondhand.exception.InvalidItemPropertiesException;
import com.esecondhand.esecondhand.service.ItemService;
import com.sun.mail.iap.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldSaveItemWhenNoExceptions() throws IOException, InvalidItemPropertiesException, InvalidImagesNumberException {
        ItemEntryDto itemEntryDto = createCorrectEntryDto();
        ResponseEntity<ItemDto> response = itemController.addItem(itemEntryDto);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void shouldReturnBadRequestWhenIncorrectData() throws IOException, InvalidItemPropertiesException, InvalidImagesNumberException {
        ItemEntryDto itemEntryDto = createIncorrectEntryDto();
        when(itemService.saveItem(itemEntryDto)).thenThrow(new InvalidImagesNumberException("message"));
        ResponseEntity<ItemDto> response = itemController.addItem(itemEntryDto);
        assertTrue(response.getStatusCode().is4xxClientError());
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

    private ItemEntryDto createIncorrectEntryDto() {
        ItemEntryDto itemEntryDto = new ItemEntryDto();
        itemEntryDto.setBrand("Test brand");
        itemEntryDto.setColorId(4L);
        itemEntryDto.setSex("WOMAN");
        itemEntryDto.setCategoryId(2L);
        itemEntryDto.setName("Test name");
        itemEntryDto.setDescription("Test description");
        itemEntryDto.setSizeId(3L);
        itemEntryDto.setPrice(90.0);
        return itemEntryDto;
    }

}
