package ru.ellada.ecommerce.controller;

import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import ru.ellada.ecommerce.domain.Candle;
import ru.ellada.ecommerce.domain.Role;
import ru.ellada.ecommerce.domain.User;
import ru.ellada.ecommerce.service.CandleService;
import ru.ellada.ecommerce.service.Impl.MailSender;
import ru.ellada.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * User controller class.
 * The @Controller annotation serves to inform Spring that this class is a bean and must be
 * loaded when the application starts.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 * @see User
 * @see Candle
 * @see UserService
 * @see CandleService
 */
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {
    /**
     * Service object for working with users.
     */
    private final UserService userService;

    /**
     * Service object for working with products.
     */
    private final CandleService candleService;

    /**
     * Mail service.
     */
    private MailSender mailSender;

    @Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;

    /**
     * Upload path for images.
     */
    @Value("${upload.path}")
    private String uploadPath;

    /**
     * Constructor for initializing the main variables of the cart controller.
     * The @Autowired annotation will allow Spring to automatically initialize objects.
     *
     * @param userService   service object for working with users.
     * @param candleService service object for working with products.
     */
    @Autowired
    public UserController(UserService userService, CandleService candleService) {
        this.userService = userService;
        this.candleService = candleService;
    }

    /**
     * Returns a list of products for editing by an administrator.
     * The @PreAuthorize annotation says the controller is accessible
     * only to users with administrator rights.
     * URL request {"/productlist"}, method GET.
     *
     * @param pageable object that specifies the information of the requested page.
     * @param model    class object {@link Model}.
     * @return productList page with model attributes.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("productlist")
    public String getAllProducts(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 12) Pageable pageable, Model model) {
        Page<Candle> page = candleService.findAll(pageable);
        int[] pagination = ControllerUtils.computePagination(page);

        model.addAttribute("pagination", pagination);
        model.addAttribute("url", "productlist");
        model.addAttribute("page", page);

        return "admin/productList";
    }

    /**
     * Returns a product for editing by an administrator.
     * The @PreAuthorize annotation says the controller is accessible
     * only to users with administrator rights.
     * URL request {"/productlist/{candle}"}, method GET.
     *
     * @param candle the product to editing.
     * @param model  class object {@link Model}.
     * @return productEdit page with model attributes.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("productlist/{candle}")
    public String editProduct(@PathVariable Candle candle, Model model) {
        model.addAttribute("candle", candle);

        return "admin/productEdit";
    }

    /**
     * Save edited product to the database by an administrator.
     * The @PreAuthorize annotation says the controller is accessible
     * only to users with administrator rights.
     * URL request {"/productlist"}, method POST.
     *
     * @param candle edited product.
     * @param file   file image.
     * @return redirect to "/user/productlist".
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("productlist")
    public String saveEditedProduct(Candle candle, @RequestParam("file") MultipartFile file) throws IOException {
        saveFile(candle, file);

        candleService.saveProductInfoById(candle.getCandleTitle(),
                candle.getFragranceNotes(), candle.getDescription(),
                candle.getFilename(), candle.getPrice(), candle.getVolume(),
                candle.getAnotherPrice(), candle.getAnotherVolume(), candle.getId());

        log.debug("ADMIN save edited product to DB: id={}, candle={}",
                candle.getId(), candle.getCandleTitle());

        return "redirect:/user/productlist";
    }

    /**
     * Returns adding product page.
     * The @PreAuthorize annotation says the controller is accessible
     * only to users with administrator rights.
     * URL request {"/add"}, method GET.
     *
     * @return addToDb page.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("add")
    public String addProductToBd() {
        return "admin/addToDb";
    }

    /**
     * Save new product to the database by an administrator.
     * The @PreAuthorize annotation says the controller is accessible
     * only to users with administrator rights.
     * URL request {"/add"}, method POST.
     *
     * @param candle        saved product.
     * @param bindingResult errors in validating http request.
     * @param model         class object {@link Model}.
     * @param file          file image.
     * @return addToDb page.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("add")
    public String addProductToBd(
            @Valid Candle candle,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);

            return "admin/addToDb";
        } else {
            saveFile(candle, file);

            candleService.save(candle);

            log.debug("ADMIN added product to DB: id={}, candle={}",
                    candle.getId(), candle.getCandleTitle());
        }

        return "admin/addToDb";
    }

    /**
     * Returns all users.
     * The @PreAuthorize annotation says the controller is accessible
     * only to users with administrator rights.
     * URL request {"/user"}, method GET.
     *
     * @param model class object {@link Model}.
     * @return userList page with model attributes.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "admin/userList";
    }

    /**
     * Returns user for edit by an administrator.
     * The @PreAuthorize annotation says the controller is accessible
     * only to users with administrator rights.
     * URL request {"/user/{user}"}, method GET.
     *
     * @param user  user id.
     * @param model class object {@link Model}.
     * @return userEdit page with model attributes.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "admin/userEdit";
    }

    /**
     * Save edited user by an administrator.
     * The @PreAuthorize annotation says the controller is accessible
     * only to users with administrator rights.
     * URL request {"/user"}, method POST.
     *
     * @param username user name.
     * @param form     user roles.
     * @param user     user id.
     * @return redirect to "/user".
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSaveEditForm(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        userService.userSave(username, form, user);

        log.debug("ADMIN save edited user form: id={}, name={}, form={}", user.getId(), username, form);

        return "redirect:/user";
    }

    /**
     * Returns profile information for current user.
     * URL request {"/user/edit"}, method GET.
     *
     * @param user  request Authenticated user.
     * @param model class object {@link Model}.
     * @return userEditProfile page with model attributes.
     */
    @GetMapping("edit")
    public String getProfileInfo(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "user/userEditProfile";
    }

    /**
     * Save edited password or email to the database by user.
     * URL request {"/user/edit"}, method POST.
     *
     * @param user     request Authenticated user.
     * @param password password to change.
     * @param email    email to change.
     * @return redirect to "/user/cabinet".
     */
    @PostMapping("edit")
    public String updateProfileInfo(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
    ) {
        userService.updateProfile(user, password, email);

        log.debug("{} change personal info: password={}, email={}", user.getUsername(), password, email);

        return "redirect:/cabinet";
    }

    /**
     * Method for saving file in upload directory.
     *
     * @param candle current product.
     * @param file   file image.
     */
    private void saveFile(Candle candle, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + File.pathSeparator + resultFilename));
            candle.setFilename(resultFilename);
        }
    }
}