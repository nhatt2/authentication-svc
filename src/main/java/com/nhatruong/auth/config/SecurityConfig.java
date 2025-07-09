package com.nhatruong.auth.config;

import static com.nhatruong.auth.constant.security.UserPermission.*;
import static com.nhatruong.auth.constant.security.UserRole.ADMIN;
import static com.nhatruong.auth.constant.security.UserRole.EMPLOYEE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.nhatruong.auth.constant.api.ApiConstants;
import com.nhatruong.auth.filter.AuthFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthFilter authFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(ApiConstants.WHITE_LIST_URLS)
                                .permitAll()
                                .requestMatchers(ApiConstants.ADMIN_BASE_URL + "/**").hasRole(ADMIN.name())
                                .requestMatchers(GET, ApiConstants.ADMIN_BASE_URL + "/**").hasAuthority(ADMIN_READ.name())
                                .requestMatchers(POST, ApiConstants.ADMIN_BASE_URL + "/**").hasAuthority(ADMIN_CREATE.name())
                                .requestMatchers(PUT, ApiConstants.ADMIN_BASE_URL + "/**").hasAuthority(ADMIN_UPDATE.name())
                                .requestMatchers(DELETE, ApiConstants.ADMIN_BASE_URL + "/**").hasAuthority(ADMIN_DELETE.name())
                                .requestMatchers(ApiConstants.EMPLOYEE_BASE_URL + "/**").hasAnyRole(ADMIN.name(), EMPLOYEE.name())
                                .requestMatchers(GET, ApiConstants.EMPLOYEE_BASE_URL + "/**").hasAnyAuthority(ADMIN_READ.name(), EMPLOYEE_READ.name())
                                .requestMatchers(PUT, ApiConstants.EMPLOYEE_BASE_URL + "/**").hasAnyAuthority(ADMIN_UPDATE.name(), EMPLOYEE_READ.name())
                                .requestMatchers(POST, ApiConstants.EMPLOYEE_BASE_URL + "/**").hasAuthority(EMPLOYEE_READ.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl(ApiConstants.AUTH_BASE_URL + "/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    SecurityContextHolder.clearContext();
                                    log.info("Method: filterChain() - User logged out successfully");
                                })
                )
        ;

        return http.build();
    }
}
