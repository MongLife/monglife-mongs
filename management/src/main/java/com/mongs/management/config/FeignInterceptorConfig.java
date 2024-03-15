package com.mongs.management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.interceptor.AccountFeignInterceptor;
import com.mongs.core.interceptor.AdminFeignInterceptor;
import com.mongs.core.util.HmacProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignInterceptorConfig {

    private final ObjectMapper objectMapper;
    private final HmacProvider hmacProvider;

    @Bean
    public AdminFeignInterceptor adminFeignInterceptor() {
        return new AdminFeignInterceptor(hmacProvider, objectMapper);
    }

    @Bean
    public AccountFeignInterceptor accountFeignInterceptor() {
        return new AccountFeignInterceptor();
    }
}
