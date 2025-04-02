package com.mongs.play.app.gateway.service;

import com.mongs.play.app.gateway.dto.req.PassportReqDto;
import com.mongs.play.core.vo.PassportVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GatewayService {

    private final WebClient authWebClient;

    public GatewayService(@Qualifier("authWebClient") WebClient authWebClient) {
        this.authWebClient = authWebClient;
    }

    public Mono<PassportVo> getPassport(String accessToken) {
        return authWebClient.post()
                .uri("/internal/auth/passport")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(PassportReqDto.builder()
                        .accessToken(accessToken)
                        .build()), PassportReqDto.class)
                .retrieve()
                .bodyToMono(PassportVo.class);
    }
}
