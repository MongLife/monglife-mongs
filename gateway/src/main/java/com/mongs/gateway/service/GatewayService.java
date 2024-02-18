package com.mongs.gateway.service;

import com.mongs.gateway.dto.request.PassportReqDto;
import com.mongs.gateway.feignClient.AuthFeignClient;
import com.mongs.passport.PassportVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayService {

    private final AuthFeignClient authFeignClient;

    public PassportVO getPassport(String accessToken) {

        log.info("GatewayService : {}", authFeignClient.passport(PassportReqDto.builder()
                        .accessToken(accessToken)
                .build()));

        return PassportVO.builder().build();
    }
}
