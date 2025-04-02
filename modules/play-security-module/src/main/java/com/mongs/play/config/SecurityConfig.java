package com.mongs.play.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.module.hmac.HmacProvider;
import com.mongs.play.module.security.exception.ForbiddenHandler;
import com.mongs.play.module.security.exception.SecurityExceptionHandler;
import com.mongs.play.module.security.exception.UnAuthorizationHandler;
import com.mongs.play.module.security.filter.PassportFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
@ComponentScan("com.mongs.play.module")
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
                    .requestMatchers("/*/prometheus").permitAll()
                    .requestMatchers("/*/**").hasAnyAuthority("ADMIN", "NORMAL")
                    .requestMatchers("/internal/**/admin/**").hasAnyAuthority("ADMIN")
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
