package com.mongs.core.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.error.ErrorResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;


@AllArgsConstructor
public class ForbiddenHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    // 인가 실패 핸들러
    // Passport 의 인가 정보에 따른 접근 권한이 없어 인가가 불가한 경우에 응답
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(SecurityErrorCode.FORBIDDEN.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResDto.of(SecurityErrorCode.FORBIDDEN)));
    }
}
