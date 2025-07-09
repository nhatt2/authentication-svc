package com.nhatruong.auth.config;

import com.nhatruong.auth.constant.message.MessageConstants;
import com.nhatruong.auth.exception.UserNotFoundException;
import com.nhatruong.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            log.debug("Method: userDetailsService() - Loading user by email: {}", email);
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(
                            String.format(MessageConstants.USER_NOT_FOUND_MESSAGE, email)));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.info("Method: authenticationProvider() - Configuring DaoAuthenticationProvider");
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        log.info("Method: authenticationManager() - Configuring AuthenticationManager");
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Method: passwordEncoder() - Configuring BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }
}
