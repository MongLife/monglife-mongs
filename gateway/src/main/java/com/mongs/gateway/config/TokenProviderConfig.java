package com.mongs.gateway.config;

import com.mongs.core.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenProviderConfig {

    @Value("${application.security.jwt.secret-key}")
    private String JWT_KEY;

    @Bean
    public JwtTokenProvider tokenProvider() {
        return new JwtTokenProvider(JWT_KEY);
    }
}
