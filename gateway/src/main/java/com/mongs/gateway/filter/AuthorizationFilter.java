package com.mongs.gateway.filter;

import com.mongs.gateway.exception.AuthorizationException;
import com.mongs.gateway.exception.ErrorCode;
import com.mongs.gateway.exception.TokenNotFoundException;
import com.mongs.gateway.util.HttpUtils;
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
    private final HttpUtils httpUtils;

    public AuthorizationFilter(TokenProvider tokenProvider, HttpUtils httpUtils) {
        super(FilterConfig.class);
        this.tokenProvider = tokenProvider;
        this.httpUtils = httpUtils;
    }

    @Override
    public GatewayFilter apply(FilterConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = httpUtils.getHeader(request, "Authorization")
                    .orElseThrow(() -> new TokenNotFoundException(ErrorCode.ACCESS_TOKEN_NOT_FOUND.getMessage()))
                    .substring(7);

            if (tokenProvider.isTokenExpired(accessToken)) {
                throw new AuthorizationException(ErrorCode.ACCESS_TOKEN_EXPIRED.getMessage());
            }

            if (config.preLogger) {
                log.info("[AuthorizationFilter] AccessToken: " + accessToken);
            }

            return chain.filter(exchange);
        };
    }
}
