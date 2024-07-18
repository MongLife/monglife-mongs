package com.mongs.play.app.gateway.filter;

import com.mongs.play.app.gateway.service.GatewayService;
import com.mongs.play.app.gateway.utils.HttpUtils;
import com.mongs.play.core.error.app.GatewayExternalErrorCode;
import com.mongs.play.core.exception.app.GatewayExternalException;
import com.mongs.play.core.vo.PassportVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class GeneratePassportFilter extends AbstractGatewayFilterFactory<FilterConfig> {

    private final GatewayService gatewayService;
    private final HttpUtils httpUtils;

    public GeneratePassportFilter(GatewayService gatewayService, HttpUtils httpUtils) {
        super(FilterConfig.class);
        this.gatewayService = gatewayService;
        this.httpUtils = httpUtils;
    }

    @Override
    public GatewayFilter apply(FilterConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = httpUtils.getHeader(request, "Authorization")
                    .orElseThrow(() -> new GatewayExternalException(GatewayExternalErrorCode.ACCESS_TOKEN_NOT_FOUND))
                    .substring(7);

            Mono<PassportVo> passportMono = gatewayService.getPassport(accessToken);

            return passportMono
                    .onErrorMap(throwable -> {
                        throw new GatewayExternalException(GatewayExternalErrorCode.PASSPORT_GENERATE_FAIL);
                    })
                    .flatMap(passportVO -> {
                        String passportJson = httpUtils.getJsonString(passportVO)
                                .orElseThrow(() -> new GatewayExternalException(GatewayExternalErrorCode.PASSPORT_PARSING_FAIL));

                        request.mutate().header("passport", URLEncoder.encode(passportJson, StandardCharsets.UTF_8)).build();

                        if (config.preLogger) {
                            log.info("[PassportFilter] Passport: {}", passportJson);
                        }

                        return chain.filter(exchange);
                    });
        };
    }
}
