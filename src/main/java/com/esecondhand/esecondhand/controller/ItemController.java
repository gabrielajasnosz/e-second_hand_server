package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.dto.*;
import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.domain.repository.ItemRepository;
import com.esecondhand.esecondhand.exception.InvalidImagesNumberException;
import com.esecondhand.esecondhand.exception.InvalidItemPropertiesException;
import com.esecondhand.esecondhand.exception.ObjectDoesntBelongToUserException;
import com.esecondhand.esecondhand.exception.ObjectDoesntExistsException;
import com.esecondhand.esecondhand.service.ItemService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    private final Integer DEFAULT_PAGE_SIZE = 10;

    private final String DEFAULT_SORTING_ORDER = "DESC";

    private final String DEFAULT_SORTING_COLUMN = "creationDate";

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ItemRepository itemRepository;


    public ItemController(ItemService itemService, ApplicationEventPublisher applicationEventPublisher, ItemRepository itemRepository) {
        this.itemService = itemService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.itemRepository = itemRepository;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<ItemDto> addItem(@Valid @ModelAttribute ItemEntryDto itemEntryDto) throws IOException {
        try {
            ItemDto itemDto = itemService.saveItem(itemEntryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(itemDto);
        } catch (InvalidImagesNumberException | InvalidItemPropertiesException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    FileSystemResource downloadImage(@PathVariable Long imageId) {
        return itemService.find(imageId);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public ResponseEntity<ItemDto> getItem(@PathVariable Long itemId) {
        try {
            ItemDto itemDto = itemService.getItem(itemId);
            return ResponseEntity.ok(itemDto);
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @RequestMapping(value = "/price/extreme-values", method = RequestMethod.GET)
    public ResponseEntity<PriceExtremeValuesDto> getExtremePriceValues() {
        PriceExtremeValuesDto priceExtremeValues = itemService.getPriceExtremeValues();
        return ResponseEntity.ok(priceExtremeValues);

    }


    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(value = "/counters/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserItemsCounters(@PathVariable Long userId) {
        try {
            CountersDto counters = itemService.getUserItemsCounters(userId);
            return ResponseEntity.status(HttpStatus.OK).body(counters);
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        }


    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @RequestMapping(value = "/hidden", method = RequestMethod.GET)
    public ResponseEntity<List<ItemPreviewDto>> getUsersHiddenItem() {
        List<ItemPreviewDto> hiddenItems = itemService.getHiddenItems();
        return ResponseEntity.ok(hiddenItems);

    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ItemDto> editItem(@Valid @RequestBody EditItemDto editItemDto) {
        try {
            ItemDto itemDto = itemService.editItem(editItemDto);
            System.out.println(itemDto);
            return ResponseEntity.status(HttpStatus.OK).body(itemDto);
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        } catch (ObjectDoesntBelongToUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) {
        try {
            itemService.deleteItem(itemId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        } catch (ObjectDoesntBelongToUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<ItemListDto> getItems(@RequestBody ItemListFiltersDto itemListFiltersDto) throws ParseException {

        if (itemListFiltersDto.getPageSize() == null) {
            itemListFiltersDto.setPageSize(DEFAULT_PAGE_SIZE);
        }
        if (itemListFiltersDto.getSortingColumn() == null) {
            itemListFiltersDto.setSortingColumn(DEFAULT_SORTING_COLUMN);
        }
        if (itemListFiltersDto.getSortingOrder() == null) {
            itemListFiltersDto.setSortingOrder(DEFAULT_SORTING_ORDER);
        }


        List<ItemPreviewDto> itemList = itemService.getItems(itemListFiltersDto);
        ItemListDto itemListDto = new ItemListDto();

        Object nextItemValue;
        Long nextItemIndex = null;
        if (itemList.size() > itemListFiltersDto.getPageSize()) {
            nextItemIndex = itemList.get(itemList.size() - 1).getId();
            if (itemListFiltersDto.getSortingColumn().equals("price")) {
                nextItemValue = itemList.get(itemList.size() - 1).getPrice();
            } else {
                nextItemValue = itemList.get(itemList.size() - 1).getCreationDate().toString();
            }
            itemListDto.setNextItemId(nextItemIndex);
            itemListDto.setNextItemValue(nextItemValue);
            Long finalNextItemIndex = nextItemIndex;
            itemList.removeIf(item -> item.getId().equals(finalNextItemIndex));
        }

        itemListDto.setItemList(itemList);
        return ResponseEntity.ok().body(itemListDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(value = "/item-visibility/{itemId}", method = RequestMethod.PUT)
    public ResponseEntity<?> changeItemVisibility(@PathVariable Long itemId, @RequestParam("status") boolean status) {
        try {
            itemService.manageItemVisibility(itemId, status);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ObjectDoesntExistsException e) {
            return ResponseEntity.notFound().build();
        } catch (ObjectDoesntBelongToUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @RequestMapping(value = "/followed-users", method = RequestMethod.GET)
    public ResponseEntity<List<ItemPreviewDto>> getFollowedUsersItems(@RequestParam("user") Long userId, @RequestParam("page") int page, int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getFollowedUsersItems(userId, page, pageSize));

    }

    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public ResponseEntity<?> reportItem(@RequestBody ReportDto reportDto, HttpServletRequest request) {
        String appUrl = request.getContextPath();
        Item item = itemRepository.findById(reportDto.getItemId()).orElse(null);
        String cause = reportDto.getCause();
        if (item != null) {
            applicationEventPublisher.publishEvent(new OnCreateReportCompleteEvent(item, cause,
                    request.getLocale(), appUrl));
        }
        itemService.reportItem(reportDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
