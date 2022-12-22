package ru.ellada.ecommerce.controller;

import ru.ellada.ecommerce.domain.Candle;
import ru.ellada.ecommerce.domain.Order;
import ru.ellada.ecommerce.domain.User;
import ru.ellada.ecommerce.repos.OrderRepository;
import ru.ellada.ecommerce.repos.UserRepository;
import ru.ellada.ecommerce.service.Impl.MailSender;
import ru.ellada.ecommerce.service.OrderService;
import ru.ellada.ecommerce.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderController orderController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void getOrderTest() throws Exception {
        List<Candle> candles = new ArrayList<>();
        User user = new User();
        Candle candle = new Candle();

        user.setCandleList(candles);
        user.getCandleList().add(candle);

        assertNotNull(user.getCandleList());
        assertEquals(1, user.getCandleList().size());
    }

    @Test
    public void postOrderTest() throws Exception {
        List<Candle> candles = new ArrayList<>();
        User user = new User();
        Candle candle = new Candle();

        user.setCandleList(candles);
        user.getCandleList().add(candle);

        userService.save(user);

        Order order = new Order(user);
        order.setId(1L);
        order.setFirstName("John");
        order.setCandleList(user.getCandleList());

        orderService.save(order);

        assertNotNull(user);
        assertNotNull(user.getCandleList());
        assertEquals(1, user.getCandleList().size());
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals("John", order.getFirstName());
        assertEquals(1, order.getCandleList().size());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
    }

    @Test
    public void finalizeOrderTest() throws Exception {
        List<Candle> candles = new ArrayList<>();
        User user = new User();
        Candle candle = new Candle();
        Order order = new Order(user);

        user.setCandleList(candles);
        user.getCandleList().add(candle);
        order.setCandleList(user.getCandleList());

        when(orderService.findAll()).thenReturn(Collections.singletonList(order));

        assertNotNull(user);
        assertNotNull(user.getCandleList());
        assertEquals(1, user.getCandleList().size());
        assertNotNull(order);
        assertEquals(1, order.getCandleList().size());
    }
}
