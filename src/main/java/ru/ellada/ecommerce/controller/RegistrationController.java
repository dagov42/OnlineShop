package ru.ellada.ecommerce.controller;

import org.springframework.core.env.Environment;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import ru.ellada.ecommerce.domain.User;
import ru.ellada.ecommerce.domain.dto.CaptchaResponseDto;
import ru.ellada.ecommerce.service.Impl.MailSender;
import ru.ellada.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Registration controller class.
 * This controller and related pages can be accessed by all users, regardless of their roles.
 * The @Controller annotation serves to inform Spring that this class is a bean and must be
 * loaded when the application starts.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 * @see User
 * @see UserService
 * @see RestTemplate
 */
@Controller
@Slf4j
public class RegistrationController {
    /**
     * URL for request to the google server.
     */
    public static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    /**
     * Service object for working with users.
     */
    private final UserService userService;

    /**
     * Mail service.
     */
    private MailSender mailSender;

    @Autowired
    private Environment env;

    /**
     * Object which offers templates for common scenarios by HTTP method.
     */
    private final RestTemplate restTemplate;

    /**
     * reCAPTCHA Secret.
     */
    @Value("${recaptcha.secret}")
    private String secret;

    /**
     * Constructor for initializing the main variables of the cart controller.
     * The @Autowired annotation will allow Spring to automatically initialize objects.
     *
     * @param userService  service object for working with users.
     * @param restTemplate object which offers templates for common scenarios by HTTP method.
     */
    @Autowired
    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    /**
     * Redirect to reset form.
     * URL request {"/reset"}, method GET.
     *
     * @return redirect to "/reset".
     */
    @GetMapping("/reset")
    public String forget() {
        return "reset";
    }

    /**
     * Reset password by email to the user.
     * URL request {"/user/resetPassword"}, method POST.
     *
     * @param userEmail user's registred email.
     * @return redirect to "/login".
     */
    @PostMapping("/reset")
    public String resetPassword(final HttpServletRequest request, final Model model, @RequestParam("email") final String userEmail) {
        final User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            model.addAttribute("message", "Пользователь с таким email не найден");
            return "redirect:/login.html";
        }

        final String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        try {
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            final SimpleMailMessage email = constructResetTokenEmail(appUrl, token, user);
            mailSender.send(user.getEmail(), email.getSubject(), email.getText());
        } catch (final MailAuthenticationException e) {
            return "redirect:/emailError.html";
        } catch (final Exception e) {
            model.addAttribute("message", e.getLocalizedMessage());
            return "redirect:/login.html";
        }
        model.addAttribute("message", "Вам отправлено письмо с инструкциями по восстановлению пароля");
        return "redirect:/login.html";
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = "Для сброса пароля перейдите по ссылке или скопируйте и вставьте в строку браузера: ";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("\"Сброс пароля");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    /**
     * Returns registration page.
     * URL request {"/registration"}, method GET.
     *
     * @return registration page.
     */
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    /**
     * Saves user credentials and redirect to login page.
     * URL request {"/registration"}, method POST.
     *
     * @param passwordConfirm user password.
     * @param captchaResponse captcha response.
     * @param user            valid user.
     * @param bindingResult   errors in validating http request.
     * @param model           class object {@link Model}.
     * @return redirect to login page with model attributes.
     */
    @PostMapping("/registration")
    public String registration(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);

        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        boolean isPasswordDifferent = user.getPassword() != null && !user.getPassword().equals(passwordConfirm);

        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Подтверждение пароля не может быть пустым");
        }

        if (isPasswordDifferent) {
            model.addAttribute("passwordError", "Пароли не совпадают");
        }

        if (isConfirmEmpty || isPasswordDifferent || bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "Пользователь существует!");
            return "registration";
        }

        log.debug("User {} registered", user.getUsername());

        return "redirect:/login";
    }

    /**
     * Returns a message that the user has activated the activation code
     * and registered on the site.
     * URL request {"/activate/{code}"}, method GET.
     *
     * @param code  activation code.
     * @param model class object {@link Model}.
     * @return login page with model attributes.
     */
    @GetMapping("/activate/{code}")
    public String activateEmailCode(@PathVariable String code, Model model) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "alert-success");
            model.addAttribute("message", "Пользователь успешно активирован");

        } else {
            model.addAttribute("messageType", "alert-danger");
            model.addAttribute("message", "Код активации не найден");

            log.error("Cant find activation code.");
        }

        return "login";
    }
}