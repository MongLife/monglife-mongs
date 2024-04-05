package com.mongs.core.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.error.ErrorResDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
public class SecurityExceptionHandler extends GenericFilterBean {

    private final ObjectMapper objectMapper;

    /**
     * 최하위 Exception 필터
     * 각 모듈 ControllerAdvice 에서 걸러지지 않은 예외들을 처리한다.
     *
     * @param servletRequest  The request to process
     * @param servletResponse The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                     to for further processing
     *
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(SecurityErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResDto.of(SecurityErrorCode.INTERNAL_SERVER_ERROR)));
        }
    }
}
