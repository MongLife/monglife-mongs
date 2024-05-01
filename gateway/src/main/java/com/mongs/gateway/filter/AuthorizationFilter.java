package com.mongs.gateway.filter;

import com.mongs.gateway.exception.AuthorizationException;
import com.mongs.gateway.exception.GatewayErrorCode;
import com.mongs.gateway.exception.TokenNotFoundException;
import com.mongs.gateway.util.HttpUtils;
import com.mongs.core.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<FilterConfig> {

    private final JwtTokenProvider jwtTokenProvider;
    private final HttpUtils httpUtils;

    public AuthorizationFilter(JwtTokenProvider jwtTokenProvider, HttpUtils httpUtils) {
        super(FilterConfig.class);
        this.jwtTokenProvider = jwtTokenProvider;
        this.httpUtils = httpUtils;
    }

    @Override
    public GatewayFilter apply(FilterConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = httpUtils.getHeader(request, "Authorization")
                    .orElseThrow(() -> new TokenNotFoundException(GatewayErrorCode.ACCESS_TOKEN_NOT_FOUND))
                    .substring(7);

            if (jwtTokenProvider.isTokenExpired(accessToken)) {
                throw new AuthorizationException(GatewayErrorCode.ACCESS_TOKEN_EXPIRED);
            }

            if (config.preLogger) {
                log.info("[AuthorizationFilter] AccessToken: " + accessToken);
            }

            return chain.filter(exchange);
        };
    }
}
