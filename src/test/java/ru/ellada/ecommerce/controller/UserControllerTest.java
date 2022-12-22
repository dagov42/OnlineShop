package ru.ellada.ecommerce.controller;

import ru.ellada.ecommerce.domain.Candle;
import ru.ellada.ecommerce.domain.Role;
import ru.ellada.ecommerce.domain.User;
import ru.ellada.ecommerce.repos.CandleRepository;
import ru.ellada.ecommerce.repos.UserRepository;
import ru.ellada.ecommerce.service.CandleService;
import ru.ellada.ecommerce.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CandleRepository candleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CandleService candleService;

    @Test
    public void getAllProductsTest() {
        List<Candle> candleList = new ArrayList<>();
        candleList.add(new Candle());

        Pageable pageable = PageRequest.of(0, 12);
        Page<Candle> page = new PageImpl<>(candleList);

        when(candleRepository.findAll(pageable)).thenReturn(page);

        assertEquals(1, candleService.findAll(pageable).getSize());
    }

    @Test
    public void addProductTest() {
        Candle candle = new Candle();
        candle.setId(1L);
        candle.setCandleTitle("test");

        when(candleRepository.save(candle)).thenReturn(candle);

        Assertions.assertEquals(candle, candleService.save(candle));
        assertNotNull(candle.getId());
    }

    @Test
    public void userListTest() {
        List<User> users = new ArrayList<>();
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        assertEquals(1, userService.findAll().size());
    }

    @Test
    public void userSaveTest() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Bob");
        user.setRoles(Collections.singleton(Role.ADMIN));

        when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.save(user));
        assertNotNull(user.getId());
        assertTrue(user.getUsername().equals("Bob"));
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.ADMIN)));
    }

    @Test
    public void updateProfileInfoTest() {
        User user = new User();
        user.setPassword("test");
        user.setEmail("test@test.com");

        when(userRepository.save(user)).thenReturn(user);

        assertEquals(user.getPassword(), "test");
        assertEquals(user.getEmail(), "test@test.com");
        assertEquals(user, userService.save(user));
    }
}