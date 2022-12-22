package ru.ellada.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Encoder configuration class.
 * Marked with @Configuration annotation - the class is the source of the bean definition.
 *
 * @author Govorukhin Dmitriy
 * @version 1.0
 */
@Configuration
public class EncryptionConfig {
    /**
     * Implementation of PasswordEncoder that uses the BCrypt strong hashing function.
     *
     * @return strength the log rounds to use, between 4 and 31.
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder(8);
//        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A);
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
}