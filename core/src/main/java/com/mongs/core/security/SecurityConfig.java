package com.mongs.core.security;

import com.mongs.core.security.exception.ForbiddenHandler;
import com.mongs.core.security.exception.SecurityExceptionHandler;
import com.mongs.core.security.exception.UnAuthorizationHandler;
import com.mongs.core.security.passport.PassportFilter;
import lombok.RequiredArgsConstructor;
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
@ComponentScan("com.mongs.core.*")
public class SecurityConfig {
    private final UnAuthorizationHandler unAuthorizationHandler;
    private final ForbiddenHandler forbiddenHandler;
    private final PassportFilter passportFilter;
    private final SecurityExceptionHandler securityExceptionHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
//                            .anyRequest().permitAll()
//                            .requestMatchers("/").permitAll()
//                            .anyRequest().authenticated()
                            .anyRequest().hasAnyAuthority("NORMAL")
            )
            .exceptionHandling(configurer -> {
                configurer.authenticationEntryPoint(unAuthorizationHandler);
                configurer.accessDeniedHandler(forbiddenHandler);
            })
            .addFilterBefore(passportFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(securityExceptionHandler, PassportFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
