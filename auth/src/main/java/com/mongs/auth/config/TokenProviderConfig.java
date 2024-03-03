package com.mongs.auth.config;

import com.mongs.core.util.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenProviderConfig {

    @Value("${application.security.jwt.secret-key}")
    private String JWT_KEY;

    @Value("${application.security.jwt.access-expiration}")
    private Long ACCESS_TOKEN_EXPIRED;

    @Value("${application.security.jwt.refresh-expiration}")
    private Long REFRESH_TOKEN_EXPIRED;

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider(JWT_KEY, ACCESS_TOKEN_EXPIRED, REFRESH_TOKEN_EXPIRED);
    }
}
