package com.mongs.gateway.filter;

import com.mongs.gateway.util.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<FilterConfig> {

    private final TokenProvider tokenProvider;

    public AuthorizationFilter(TokenProvider tokenProvider) {
        super(FilterConfig.class);
        this.tokenProvider = tokenProvider;
    }

    @Override
    public GatewayFilter apply(FilterConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0);

            accessToken = accessToken.substring(7);

            if (tokenProvider.isTokenExpired(accessToken)) {
                throw new RuntimeException("");
            }

            if (config.preLogger) {
                log.info("[AuthorizationFilter] AccessToken: " + accessToken);
            }

            return chain.filter(exchange);
        };
    }
}
