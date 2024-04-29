package com.mongs.core.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.error.ErrorResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@AllArgsConstructor
public class UnAuthorizationHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * 인증 불가 핸들러
     * 유효한 Passport 가 없어 인증에 필요한 정보가 없는 경우에 응답
     *
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(SecurityErrorCode.UNAUTHORIZED.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResDto.of(SecurityErrorCode.UNAUTHORIZED)));
    }
}
