package com.mongs.gateway.config;

import com.mongs.core.util.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenProviderConfig {

    @Value("${application.security.jwt.secret-key}")
    private String JWT_KEY;

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider(JWT_KEY);
    }
}
