package com.mongs.management.config;

import com.mongs.core.interceptor.AccountFeignInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignInterceptorConfig {

    @Bean
    public AccountFeignInterceptor accountFeignInterceptor() {
        return new AccountFeignInterceptor();
    }
}
