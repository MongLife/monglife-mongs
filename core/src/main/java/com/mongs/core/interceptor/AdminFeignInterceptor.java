package com.mongs.core.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.interceptor.exception.FeignErrorCode;
import com.mongs.core.interceptor.exception.GenerateException;
import com.mongs.core.utils.HmacProvider;
import com.mongs.core.vo.passport.PassportAccount;
import com.mongs.core.vo.passport.PassportData;
import com.mongs.core.vo.passport.PassportVO;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
public final class AdminFeignInterceptor implements RequestInterceptor {

    private final HmacProvider hmacProvider;
    private final ObjectMapper objectMapper;

    public AdminFeignInterceptor(HmacProvider hmacProvider, ObjectMapper objectMapper) {
        this.hmacProvider = hmacProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void apply(RequestTemplate template) {
        try {
            PassportVO passportVO = PassportVO.builder()
                    .data(PassportData.builder()
                            .account(PassportAccount.builder()
                                    .id(0L)
                                    .deviceId("-")
                                    .email(UUID.randomUUID().toString())
                                    .name("-")
                                    .loginCount(0)
                                    .role("ADMIN")
                                    .build())
                            .build())
                    .build();

            String passportIntegrity = hmacProvider.generateHmac(passportVO.data())
                    .orElseThrow(() -> new GenerateException(FeignErrorCode.GENERATE_PASSPORT_FAIL));

            String passportJson = objectMapper.writeValueAsString(passportVO.toBuilder()
                    .passportIntegrity(passportIntegrity)
                    .build());

            template.header("passport", URLEncoder.encode(passportJson, StandardCharsets.UTF_8));
        } catch (JsonProcessingException ignored) {}
    }
}