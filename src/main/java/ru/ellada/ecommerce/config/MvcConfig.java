package ru.ellada.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Configuration class of view components, MVC setup.
 * Indicates Spring where the view components are located and how to render them.
 * The class implements the {@link WebMvcConfigurer} interface.
 * Marked with @Configuration annotation - the class is the source of the bean definition.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 */
@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
public class MvcConfig implements WebMvcConfigurer {
    /**
     * Upload path for images.
     */
    @Value("${upload.path}")
    private String uploadPath;

    /**
     * Returns RestTemplate which offers templates for common scenarios by HTTP method.
     *
     * @return RestTemplate.
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * Setting up a login controller.
     * Configure simple automated controllers pre-configured with the response
     * status code and/or a view to render the response body.
     *
     * @param registry assists with the registration of simple automated controllers
     *                 pre-configured with status code and/or a view.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    /**
     * Specifies where the resources will be stored.
     * Add handlers to serve static resources such as images, js, and, css
     * files from specific locations under web application root, the classpath,
     * and others.
     *
     * @param registry stores registrations of resource handlers for serving static
     *                 resources such as images, css files and others.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file://" + uploadPath + "/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

}