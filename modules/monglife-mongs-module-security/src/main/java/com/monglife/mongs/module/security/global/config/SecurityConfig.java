package com.monglife.mongs.module.security.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monglife.core.enums.role.RoleCode;
import com.monglife.mongs.module.security.filter.PassportFilter;
import com.monglife.mongs.module.security.global.exception.ForbiddenHandler;
import com.monglife.mongs.module.security.global.exception.SecurityExceptionHandler;
import com.monglife.mongs.module.security.global.exception.UnAuthorizationHandler;
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
public class SecurityConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

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
                    .requestMatchers("/*/**").hasAnyAuthority(RoleCode.ADMIN.getRole(), RoleCode.NORMAL.getRole())
                    .requestMatchers("/internal/**/admin/**").hasAnyAuthority(RoleCode.ADMIN.getRole())
                    .anyRequest().authenticated()
            )
            .exceptionHandling(configurer -> {
                configurer.authenticationEntryPoint(unAuthorizationHandler);
                configurer.accessDeniedHandler(forbiddenHandler);
            })
            .build();
    }
    @Bean
    public UnAuthorizationHandler unAuthorizationHandler(ObjectMapper objectMapper) {
        return new UnAuthorizationHandler(objectMapper);
    }
    @Bean
    public ForbiddenHandler forbiddenHandler(ObjectMapper objectMapper) {
        return new ForbiddenHandler(objectMapper);
    }
    @Bean
    public SecurityExceptionHandler securityExceptionHandler(ObjectMapper objectMapper) {
        return new SecurityExceptionHandler(objectMapper);
    }
    @Bean
    public PassportFilter passportFilter(ObjectMapper objectMapper) {
        return new PassportFilter(objectMapper);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
