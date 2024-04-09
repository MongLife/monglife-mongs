package com.mongs.management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.security.exception.ForbiddenHandler;
import com.mongs.core.security.exception.SecurityExceptionHandler;
import com.mongs.core.security.exception.UnAuthorizationHandler;
import com.mongs.core.security.filter.PassportFilter;
import com.mongs.core.utils.HmacProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;
    private final HmacProvider hmacProvider;

    @Bean
    public SecurityFilterChain filterChain(
            @Autowired UnAuthorizationHandler unAuthorizationHandler,
            @Autowired ForbiddenHandler forbiddenHandler,
            @Autowired PassportFilter passportFilter,
            @Autowired SecurityExceptionHandler securityExceptionHandler,
            HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .addFilterBefore(passportFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(securityExceptionHandler, PassportFilter.class)
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/management/prometheus").permitAll()
                    .requestMatchers("/management/admin/**").hasAnyAuthority("ADMIN")
                    .requestMatchers("/management/**").hasAnyAuthority("NORMAL")
                    .anyRequest().authenticated()
            )
            .exceptionHandling(configurer -> {
                configurer.authenticationEntryPoint(unAuthorizationHandler);
                configurer.accessDeniedHandler(forbiddenHandler);
            })
            .build();
    }
    @Bean
    public UnAuthorizationHandler unAuthorizationHandler() {
        return new UnAuthorizationHandler(objectMapper);
    }
    @Bean
    public ForbiddenHandler forbiddenHandler() {
        return new ForbiddenHandler(objectMapper);
    }
    @Bean
    public SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandler(objectMapper);
    }
    @Bean
    public PassportFilter passportFilter() {
        return new PassportFilter(objectMapper, hmacProvider);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
