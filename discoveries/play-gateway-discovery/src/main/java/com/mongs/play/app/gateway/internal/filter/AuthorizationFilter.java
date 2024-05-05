package com.mongs.play.app.gateway.internal.filter;

import com.mongs.play.app.gateway.internal.utils.HttpUtils;
import com.mongs.play.core.error.app.GatewayExternalErrorCode;
import com.mongs.play.core.exception.app.GatewayExternalException;
import com.mongs.play.module.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<FilterConfig> {

    private final JwtTokenProvider tokenProvider;
    private final HttpUtils httpUtils;

    public AuthorizationFilter(JwtTokenProvider tokenProvider, HttpUtils httpUtils) {
        super(FilterConfig.class);
        this.tokenProvider = tokenProvider;
        this.httpUtils = httpUtils;
    }

    @Override
    public GatewayFilter apply(FilterConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = httpUtils.getHeader(request, "Authorization")
                    .orElseThrow(() -> new GatewayExternalException(GatewayExternalErrorCode.ACCESS_TOKEN_NOT_FOUND))
                    .substring(7);

            if (tokenProvider.isTokenExpired(accessToken)) {
                throw new GatewayExternalException(GatewayExternalErrorCode.ACCESS_TOKEN_EXPIRED);
            }

            if (config.preLogger) {
                log.info("[AuthorizationFilter] AccessToken: {}", accessToken);
            }

            return chain.filter(exchange);
        };
    }
}
