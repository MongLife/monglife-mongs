package com.mongs.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.gateway.service.GatewayService;
import com.mongs.passport.PassportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class PassportFilter extends AbstractGatewayFilterFactory<FilterConfig> {

    private final GatewayService gatewayService;
    private final ObjectMapper objectMapper;

    public PassportFilter(GatewayService gatewayService, ObjectMapper objectMapper) {
        super(FilterConfig.class);
        this.gatewayService = gatewayService;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(FilterConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = Objects.requireNonNull(request.getHeaders().get("Authorization")).get(0);
            accessToken = accessToken.substring(7);

            PassportVO passportVO = gatewayService.getPassport(accessToken);

            try {
                request.mutate().header("passport", objectMapper.writeValueAsString(passportVO)).build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            if (config.preLogger) {
                log.info("[PassportFilter] Passport: " + passportVO);
            }

            return chain.filter(exchange);
        };
    }
}
