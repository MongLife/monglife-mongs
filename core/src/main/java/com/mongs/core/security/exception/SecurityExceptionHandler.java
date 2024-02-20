package com.mongs.core.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.core.error.ErrorResDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler extends GenericFilterBean {

    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            chain.doFilter(request, response);
        } catch (PassportNotFoundException e) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(e.errorCode.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResDto.of(e.errorCode)));
        } catch (Exception e) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResDto.of(SecurityErrorCode.FORBIDDEN)));
        }
    }
}
