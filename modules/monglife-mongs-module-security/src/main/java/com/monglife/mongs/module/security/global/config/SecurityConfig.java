package com.monglife.mongs.module.security.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monglife.core.enums.role.RoleCode;
import com.monglife.mongs.module.security.filter.PassportFilter;
import com.monglife.mongs.module.security.global.exception.ForbiddenHandler;
import com.monglife.mongs.module.security.filter.GlobalExceptionFilter;
import com.monglife.mongs.module.security.global.exception.UnAuthorizationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public SecurityFilterChain filterChain(
            @Autowired UnAuthorizationHandler unAuthorizationHandler,
            @Autowired ForbiddenHandler forbiddenHandler,
            @Autowired PassportFilter passportFilter,
            @Autowired GlobalExceptionFilter globalExceptionFilter,
            HttpSecurity http) throws Exception {

        return http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .addFilterBefore(passportFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(globalExceptionFilter, PassportFilter.class)
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/prometheus").permitAll()
                    .requestMatchers("/open/**").permitAll()
                    .requestMatchers("/internal/**").permitAll()
                    .requestMatchers("/**").hasAnyAuthority(RoleCode.ADMIN.getRole(), RoleCode.NORMAL.getRole())
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
    public GlobalExceptionFilter securityExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionFilter(objectMapper);
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
