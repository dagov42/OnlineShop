package ru.ellada.ecommerce.controller;

import ru.ellada.ecommerce.domain.Candle;
import ru.ellada.ecommerce.domain.User;
import ru.ellada.ecommerce.repos.UserRepository;
import ru.ellada.ecommerce.service.Impl.MailSender;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartController cartController;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void getCartTest() throws Exception {
        List<Candle> candles = new ArrayList<>();
        candles.add(new Candle());
        candles.add(new Candle());

        User user = new User();
        user.setCandleList(candles);

        assertNotNull(user.getCandleList());
        assertEquals(2, user.getCandleList().size());
    }

    @Test
    public void addToCartTest() throws Exception {
        List<Candle> candles = new ArrayList<>();
        User user = new User();

        user.setCandleList(candles);
        user.getCandleList().add(new Candle());

        userService.addUser(user);

        assertNotNull(user.getCandleList());
        assertEquals(1, user.getCandleList().size());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void removeFromCartTest() throws Exception {
        List<Candle> candles = new ArrayList<>();
        User user = new User();
        Candle candle = new Candle();

        user.setCandleList(candles);
        user.getCandleList().add(candle);

        userService.addUser(user);

        user.getCandleList().remove(candle);

        userService.addUser(user);

        assertNotNull(user.getCandleList());
        assertEquals(0, user.getCandleList().size());

        Mockito.verify(userRepository, Mockito.times(2)).save(user);
    }
}
