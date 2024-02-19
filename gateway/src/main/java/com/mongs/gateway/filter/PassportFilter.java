package com.mongs.gateway.filter;

import com.mongs.gateway.exception.ErrorCode;
import com.mongs.gateway.exception.PassportException;
import com.mongs.gateway.exception.TokenNotFoundException;
import com.mongs.gateway.service.GatewayService;
import com.mongs.gateway.util.HttpUtils;
import com.mongs.passport.PassportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PassportFilter extends AbstractGatewayFilterFactory<FilterConfig> {

    private final GatewayService gatewayService;
    private final HttpUtils httpUtils;

    public PassportFilter(GatewayService gatewayService, HttpUtils httpUtils) {
        super(FilterConfig.class);
        this.gatewayService = gatewayService;
        this.httpUtils = httpUtils;
    }

    @Override
    public GatewayFilter apply(FilterConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = httpUtils.getHeader(request, "Authorization")
                    .orElseThrow(() -> new TokenNotFoundException(ErrorCode.ACCESS_TOKEN_NOT_FOUND.getMessage()))
                    .substring(7);

            Mono<PassportVO> passportMono = gatewayService.getPassport(accessToken);

            return passportMono.flatMap(passportVO -> {
                String passportJson = httpUtils.getJsonString(passportVO)
                        .orElseThrow(() -> new PassportException(ErrorCode.PASSPORT_GENERATE_FAIL.getMessage()));

                request.mutate().header("passport", passportJson).build();

                if (config.preLogger) {
                    log.info("[PassportFilter] Passport: " + passportJson);
                }

                return chain.filter(exchange);
            });
        };
    }
}
