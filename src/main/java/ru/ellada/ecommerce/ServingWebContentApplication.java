package ru.ellada.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Class for launch an application.
 * @SpringBootApplication is a convenience annotation that adds all of the following:
 * @Configuration: tags the class as a source of bean definitions for the application context.
 * @EnableAutoConfiguration: tells Spring Boot to start adding beans based on classpath settings,
 * other beans, and various property settings.
 * @ComponentScan: tells Spring to look for other components, configurations,
 * and services in the package, letting it find the controllers.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 */
@SpringBootApplication
public class ServingWebContentApplication {
    /**
     * The main() method uses Spring Boot’s SpringApplication.run() method to launch an application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class, args);
    }
}
