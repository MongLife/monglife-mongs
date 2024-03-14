package com.mongs.lifecycle.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.exception.CoreErrorCode;
import com.mongs.core.exception.GenerateException;
import com.mongs.core.util.HmacProvider;
import com.mongs.core.vo.passport.PassportAccount;
import com.mongs.core.vo.passport.PassportData;
import com.mongs.core.vo.passport.PassportVO;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public final class AdminFeignInterceptor implements RequestInterceptor {

    private final HmacProvider hmacProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void apply(RequestTemplate template) {
        try {
            PassportVO passportVO = PassportVO.builder()
                    .data(PassportData.builder()
                            .account(PassportAccount.builder()
                                    .id(0L)
                                    .deviceId("-")
                                    .email(UUID.randomUUID().toString())
                                    .name("LIFECYCLE")
                                    .loginAt(LocalDate.now())
                                    .role("ADMIN")
                                    .build())
                            .build())
                    .build();

            String passportIntegrity = hmacProvider.generateHmac(passportVO.data())
                    .orElseThrow(() -> new GenerateException(CoreErrorCode.GENERATE_PASSPORT_FAIL));

            String passportJson = objectMapper.writeValueAsString(passportVO.toBuilder()
                    .passportIntegrity(passportIntegrity)
                    .build());

            template.header("passport", URLEncoder.encode(passportJson, StandardCharsets.UTF_8));
        } catch (JsonProcessingException ignored) {}
    }
}