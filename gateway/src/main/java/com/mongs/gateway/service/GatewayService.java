package com.mongs.gateway.service;

import com.mongs.passport.PassportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GatewayService {

    public PassportVO getPassport(String accessToken) {
        return PassportVO.builder().build();
    }
}
