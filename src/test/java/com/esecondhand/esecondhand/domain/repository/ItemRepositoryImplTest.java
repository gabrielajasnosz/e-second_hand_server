package com.esecondhand.esecondhand.domain.repository;

import com.esecondhand.esecondhand.ESecondHandApplication;
import com.esecondhand.esecondhand.domain.dto.ItemListFiltersDto;
import com.esecondhand.esecondhand.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.text.ParseException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ESecondHandApplication.class)
class ItemRepositoryImplTest {

    @Autowired
    private ItemRepository itemRepositoryImpl;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ItemRepository itemRepository;

    @MockBean
    private Clock clock;


    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailSender mailSender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        User user = userRepository.save(createAppUser("name1", "email1@email.com"));
        User user2 = userRepository.save(createAppUser("name2", "email2@email.com"));

        Color color = colorRepository.save(new Color(null, "black", "hex"));
        Color color2 = colorRepository.save(new Color(null, "pink", "hex"));

        Size size = sizeRepository.save(new Size(null, 1L, "S/36"));
        Size size2 = sizeRepository.save(new Size(null, 1L, "S/36"));

        Brand brand1 = brandRepository.save(new Brand(null, "Zara"));
        Brand brand2 = brandRepository.save(new Brand(null, "Mango"));


        Category category1 = categoryRepository.save(new Category(null, "Pants", Gender.valueOf("UNDEFINED"), null, null));
        Category category2 = categoryRepository.save(new Category(null, "Skirts", Gender.valueOf("WOMAN"), null, null));

        itemRepository.save(new Item(null, "test1", user, null, category1, brand1, color, 90.0, size, Gender.valueOf("UNDEFINED"), LocalDateTime.now(), true, false, null));
        itemRepository.save(new Item(null, "test2", user, null, category1, brand1, color, 80.0, size, Gender.valueOf("WOMAN"), LocalDateTime.now(), true, false, null));
        itemRepository.save(new Item(null, "test3", user, null, category1, brand1, color, 70.0, size, Gender.valueOf("MAN"), LocalDateTime.now(), true, false, null));

    }

    @Test
    public void shouldSortItems() throws ParseException {
        ItemListFiltersDto itemListFiltersDto = new ItemListFiltersDto();
        itemListFiltersDto.setPageSize(10);
        itemListFiltersDto.setSortingColumn("creationDate");
        itemListFiltersDto.setSortingOrder("DESC");

        List<Item> items = itemRepository.findItems(itemListFiltersDto, new ArrayList<>(), new ArrayList<>());

        assertEquals(3, items.size());
        assertEquals("test3", items.get(0).getName());
        assertEquals(3, items.get(0).getId());
        assertEquals(1, items.get(2).getId());


    }

    @Test
    public void shouldFilterItems() throws ParseException {
        ItemListFiltersDto itemListFiltersDto = new ItemListFiltersDto();
        itemListFiltersDto.setPageSize(10);
        itemListFiltersDto.setSortingColumn("creationDate");
        itemListFiltersDto.setSortingOrder("DESC");
        itemListFiltersDto.setGender("WOMAN");

        List<Item> items = itemRepository.findItems(itemListFiltersDto, new ArrayList<>(), new ArrayList<>());

        List<Item> expectedItems = items.stream().filter((e) -> e.getGender().toString().equals("WOMAN") || e.getGender().toString().equals("UNDEFINED")).collect(Collectors.toList());

        assertEquals(items.size(), expectedItems.size());

    }


    private User createAppUser(String displayname, String email) {
        User user = new User();
        user.setRole("USER");
        user.setPassword("test");
        user.setGender(Gender.valueOf("MAN"));
        user.setZipCode("33-100");
        user.setEnabled(true);
        user.setCreationDate(LocalDateTime.now());
        user.setDisplayName(displayname);
        user.setEmail(email);
        return user;

    }

}
