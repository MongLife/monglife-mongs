package com.mongs.gateway.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;
import com.mongs.core.error.ErrorResDto;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.Order;
import org.springframework.core.codec.Hints;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

@Slf4j
@Component
@NonNullApi
@Order(-1)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        ServerHttpRequest request = exchange.getRequest();

        String method = request.getMethod().name();
        String id = request.getId();
        String path = request.getPath().value();

        log.error("{} : {} : {} : {}", e.getMessage(), id, method, path);
        
        /* 시스템 정의 예외 처리 */
        if (e instanceof NotFoundException || e instanceof ConnectException || e instanceof WebClientRequestException) {
            return setErrorResponse(exchange, GatewayErrorCode.CONNECT_FAIL);
        } else if (e instanceof TokenNotFoundException || e instanceof AuthorizationException || e instanceof PassportException) {
            return setErrorResponse(exchange, (( ErrorException) e).errorCode);
        } else {
            return setErrorResponse(exchange, GatewayErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Mono<Void> setErrorResponse(ServerWebExchange exchange, ErrorCode errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(errorCode.getHttpStatus());
        ErrorResDto errorResDto = ErrorResDto.of(errorCode);

        return response.writeWith(
                new Jackson2JsonEncoder()
                        .encode(Mono.just(errorResDto),
                                response.bufferFactory(),
                                ResolvableType.forInstance(errorResDto),
                                MediaType.APPLICATION_JSON,
                                Hints.from(Hints.LOG_PREFIX_HINT, exchange.getLogPrefix()))
        );
    }
}
