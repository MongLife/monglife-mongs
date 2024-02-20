package com.mongs.gateway.service;

import com.mongs.gateway.dto.request.PassportReqDto;
import com.mongs.core.passport.PassportVO;
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

    public Mono<PassportVO> getPassport(String accessToken) {
        return authWebClient.post()
                .uri("/auth/passport")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(PassportReqDto.builder()
                        .accessToken(accessToken)
                        .build()), PassportReqDto.class)
                .retrieve()
                .bodyToMono(PassportVO.class);
    }
}
