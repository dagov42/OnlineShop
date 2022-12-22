package ru.ellada.ecommerce.controller;

import lombok.extern.slf4j.Slf4j;
import ru.ellada.ecommerce.domain.Candle;
import ru.ellada.ecommerce.domain.User;
import ru.ellada.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Customer shopping cart controller class.
 * This controller and related pages can be accessed by all users, regardless of their roles.
 * The @Controller annotation serves to inform Spring that this class is a bean and must be
 * loaded when the application starts.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 * @see User
 * @see UserService
 */
@Controller
@Slf4j
public class CartController {
    /**
     * Service object for working with customer shopping cart.
     */
    private final UserService userService;

    /**
     * Constructor for initializing the main variables of the cart controller.
     * The @Autowired annotation will allow Spring to automatically initialize objects.
     *
     * @param userService service object for working with user shopping cart.
     */
    @Autowired
    public CartController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns customer shopping cart.
     * URL request {"/cart"}, method GET.
     *
     * @param userSession requested Authenticated customer.
     * @param model       class object {@link Model}.
     * @return cart page with model attributes.
     */
    @GetMapping("/cart")
    public String getCart(@AuthenticationPrincipal User userSession, Model model) {
        User userFromDB = userService.findByUsername(userSession.getUsername());
        model.addAttribute("candles", userFromDB.getCandleList());

        return "cart";
    }

    /**
     * Adds a product to the customer shopping cart and redirects it to "/cart".
     * URL request {"/cart/add"}, method POST.
     *
     * @param candle      the product to add to the cart.
     * @param userSession request Authenticated customer.
     * @return redirect to cart page.
     */
    @PostMapping("/cart/add")
    public String addToCart(
//            @RequestParam Map allParams) {
//        return "Parameters are " + allParams.entrySet();
    @RequestParam("add") Candle candle, @AuthenticationPrincipal User userSession) {
        User user = userService.findByUsername(userSession.getUsername());
        user.getCandleList().add(candle);
        userService.save(user);
        return "redirect:/cart";
    }

    /**
     * Remove product from customer shopping cart and redirects it to "/cart".
     * URL request {"/cart/remove"}, method POST.
     *
     * @param candle      the product to be removed from the customer shopping cart.
     * @param userSession request Authenticated customer.
     * @return redirect to cart page.
     */
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam(value = "candleId") Candle candle, @AuthenticationPrincipal User userSession) {
        User user = userService.findByUsername(userSession.getUsername());
        if (candle != null) {
            user.getCandleList().remove(candle);
        }
        userService.save(user);

        return "redirect:/cart";
    }
}