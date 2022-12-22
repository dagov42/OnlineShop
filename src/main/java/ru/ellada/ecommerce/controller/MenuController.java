package ru.ellada.ecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ellada.ecommerce.domain.Candle;
import ru.ellada.ecommerce.service.CandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu page controller class.
 * This controller and related pages can be accessed by all users, regardless of their roles.
 * The @Controller annotation serves to inform Spring that this class is a bean and must be
 * loaded when the application starts.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 * @see Candle
 * @see CandleService
 */
@Controller
@Slf4j
@RequestMapping("/menu")
public class MenuController {
    /**
     * Service object for working with products.
     */
    private final CandleService candleService;

    /**
     * Constructor for initializing the main variables of the product controller.
     * The @Autowired annotation will allow Spring to automatically initialize objects.
     *
     * @param candleService Service object for working with products.
     */
    @Autowired
    public MenuController(CandleService candleService) {
        this.candleService = candleService;
    }

    /**
     * Returns all products to the menu page with pagination.
     * URL request {"/menu"}, method GET.
     *
     * @param pageable object that specifies the information of the requested page.
     * @param model    class object {@link Model}.
     * @return menu page with model attributes.
     */
    @GetMapping
    public String mainMenu(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 12) Pageable pageable, Model model) {
        Page<Candle> page = candleService.findAll(pageable);
        int[] pagination = ControllerUtils.computePagination(page);
        getMinMaxCandlePrice(model);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "/menu");
        model.addAttribute("page", page);

        return "menu";
    }

    /**
     * Returns list of candles to the menu page with pagination, by selected parameters.
     * URL request {"/menu/search"}, method GET.
     *
     * @param pageable      object that specifies the information of the requested page.
     * @param startingPrice the starting price of the product that the user enters.
     * @param endingPrice   the ending price of the product that the user enters.
     * @param model         class object {@link Model}
     * @return menu page with model attributes.
     */
    @GetMapping("search")
    public String searchByParameters(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 12) Pageable pageable,
            @RequestParam(value = "startingPrice", required = false, defaultValue = "0") Integer startingPrice,
            @RequestParam(value = "endingPrice", required = false, defaultValue = "0") Integer endingPrice,
            Model model
    ) {
//        StringBuilder urlBuilder = new StringBuilder();
//        Page<Candle> candlesSearch = null;
        getMinMaxCandlePrice(model);

//        if (gender.isEmpty()) {
            Page<Candle> priceRange = candleService.findByPriceBetween(startingPrice, endingPrice, pageable);
            int[] pagination = ControllerUtils.computePagination(priceRange);

            model.addAttribute("pagination", pagination);
            model.addAttribute("url", "/menu/search?startingPrice=" + startingPrice + "&endingPrice=" + endingPrice);
            model.addAttribute("page", priceRange);
            return "menu";
//        }

//        int[] pagination = ControllerUtils.computePagination(candlesSearch);
//
//        model.addAttribute("pagination", pagination);
//        model.addAttribute("url", "/menu/search" + urlBuilder);
//        model.addAttribute("page", candlesSearch);
//
//        return "menu";
    }

    private void getMinMaxCandlePrice(Model model) {
        BigDecimal minCandlePrice = candleService.minCandlePrice();
        BigDecimal maxCandlePrice = candleService.maxCandlePrice();

        model.addAttribute("minCandlePrice", minCandlePrice);
        model.addAttribute("maxCandlePrice", maxCandlePrice);
    }
}