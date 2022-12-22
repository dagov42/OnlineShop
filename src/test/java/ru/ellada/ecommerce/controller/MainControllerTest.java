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
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandleRepository candleRepository;

    @Autowired
    private CandleService candleService;

    @Autowired
    private MainController mainController;

    @Test
    public void homeTest() throws Exception {
        Candle candle1 = generateCandle(39L,  "Test1");
        Candle candle2 = generateCandle(40L, "Test2");

        when(candleRepository.findAll()).thenReturn(Arrays.asList(candle1, candle2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("candles", hasSize(2)))
                .andExpect(model().attribute("candles", hasItem(
                        allOf(
                                hasProperty("id", is(39L)),
                                hasProperty("candleTitle", is("123"))
                        )
                )))
                .andExpect(model().attribute("candle", hasItem(
                        allOf(
                                hasProperty("id", is(40L)),
                                hasProperty("candleTitle", is("1234"))
                        )
                )));
    }

    @Test
    public void searchTest() throws Exception {
        List<Candle> candleList = new ArrayList<>();
        Candle candle = new Candle();
        candle.setCandleTitle("120");
        candleList.add(candle);

        Pageable pageable = PageRequest.of(0, 12);
        Page<Candle> page = new PageImpl<>(candleList);

        when(candleRepository.findByCandleTitle(candle.getCandleTitle(), pageable)).thenReturn(page);

        assertNotNull(candleList);
        assertNotNull(candle.getCandleTitle());
        assertEquals(1, candleService.findByCandleTitle(candle.getCandleTitle(), pageable).getSize());
        Assertions.assertEquals("120", candleService.findByCandleTitle(candle.getCandleTitle(), pageable).getContent().get(0).getCandleTitle());
    }

    @Test
    public void getProductByIdTest() throws Exception {
        Long id = 7L;
        Candle candle = new Candle();
        candle.setId(7L);

        assertNotNull(candle.getId());
        assertEquals(id, candle.getId());
    }

    private Candle generateCandle(Long id, String candleTitle) {
        Candle candle = new Candle();
        candle.setId(id);
        candle.setCandleTitle(candleTitle);
        candle.setPrice(1000);

        return candle;
    }
}
