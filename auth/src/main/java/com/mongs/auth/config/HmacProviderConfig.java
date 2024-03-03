package com.mongs.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.util.HmacProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HmacProviderConfig {

    private final ObjectMapper objectMapper;

    @Value("${application.security.hmac.secret-key}")
    private String secretKey;

    @Bean
    public HmacProvider hmacProvider() {
        return new HmacProvider(objectMapper, secretKey);
    }
}
