package ru.ellada.ecommerce.controller;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Home page controller class.
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
public class MainController {
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
    public MainController(CandleService candleService) {
        this.candleService = candleService;
    }

    /**
     * Returns all products to the main page.
     * URL request {"/"}, method GET.
     *
     * @return main page with model attributes.
     */
    @GetMapping("/")
    public String home(Model model) {
        List<Candle> candles = candleService.findAll();
        model.addAttribute("candles", candles);

        return "main";
    }

    /**
     * Returns page with web-store contact details.
     * URL request {"contacts"}, method GET.
     *
     * @return contacts page with model attributes..
     */
    @GetMapping("/contacts")
    public String getContacts() {
        return "contacts";
    }

    /**
     * Returns user cabinet page.
     * URL request {"cabinet"}, method GET.
     *
     * @return userCabinet page with model attributes.
     */
    @GetMapping("/cabinet")
    public String userCabinet() {
        return "user/userCabinet";
    }

    /**
     * Returns "menu" page with a product that matches the candle title.
     * URL request {"/search"}, method GET.
     *
     * @param filter    requested parameter for product search.
     * @param pageable  object that specifies the information of the requested page.
     * @param model     class object {@link Model}.
     * @return menu page with model attributes.
     */
    @GetMapping("/search")
    public String search(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 12) Pageable pageable,
            @RequestParam String filter,
            Model model
    ) {
        Page<Candle> page = candleService.findByCandleTitleLike(filter, pageable);
        // todo make search by fragrance
        int[] pagination = ControllerUtils.computePagination(page);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "/menu");
        model.addAttribute("page", page);
        return "menu";
    }

    /**
     * Returns to page "/product/{id}" with a product that matches the input id parameter.
     * URL request {"/product/{id}"}, method GET.
     *
     * @param candle product "id" to be returned to the page.
     * @return product page with model attributes.
     */
    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") Candle candle, Model model) {
        model.addAttribute("candle", candle);
        return "product";
    }
}