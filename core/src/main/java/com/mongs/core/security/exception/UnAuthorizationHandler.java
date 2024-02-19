package com.mongs.core.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.security.dto.response.SecurityErrorCode;
import com.mongs.core.security.dto.response.SecurityErrorResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnAuthorizationHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    // 인증 불가 핸들러
    // 유효한 Passport 가 없어 인증에 필요한 정보가 없는 경우에 응답
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(SecurityErrorResDto.of(SecurityErrorCode.UNAUTHORIZATION)));

        log.info("Passport 인증 불가 : {}", request.getHeader("passport"));
    }
}
