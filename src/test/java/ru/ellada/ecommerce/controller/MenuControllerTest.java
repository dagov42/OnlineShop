package ru.ellada.ecommerce.controller;

import ru.ellada.ecommerce.domain.Candle;
import ru.ellada.ecommerce.repos.CandleRepository;
import ru.ellada.ecommerce.service.CandleService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandleRepository candleRepository;

    @Autowired
    private CandleService candleService;

    @Autowired
    private MenuController menuController;

    @Test
    public void mainMenuTest() throws Exception {
        List<Candle> candles = new ArrayList<>();
        Candle candle = new Candle();
        candles.add(candle);

        Pageable pageable = PageRequest.of(0, 12);
        Page<Candle> page = new PageImpl<>(candles);

        when(candleRepository.findAll(pageable)).thenReturn(page);

        assertNotNull(candles);
        assertEquals(1, candleService.findAll(pageable).getSize());
    }

//    @Test
//    public void searchByParametersTest() throws Exception {
//        List<Candle> candles = new ArrayList<>();
//        Candle candle = new Candle();
//        candle.setPrice(1000);
//        candles.add(candle);
//
//        Pageable pageable = PageRequest.of(0, 12);
//        Page<Candle> page = new PageImpl<>(candles);
//
//        when(candleRepository.findByPriceBetween(900, 1100, pageable)).thenReturn(page);
//        assertNotNull(candles);
//        Assertions.assertEquals(1000, candleService.findByPriceBetween(900, 1100, pageable)
//                .getContent().get(0).getPrice());
//        assertEquals(1, candleService.findByCandleGenderIn(
//                Collections.singletonList(candles.get(0).getCandleGender()),
//                pageable).getSize());
//    }
}