package com.mongs.play.module.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongs.play.core.dto.res.ErrorResDto;
import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.error.GlobalErrorCode;
import com.mongs.play.core.error.module.SecurityErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            chain.doFilter(request, response);
        } catch (PassportIntegrityException e) {
            ErrorCode errorCode = e.errorCode;
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(errorCode.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResDto.of(errorCode)));
        } catch (Exception e) {
            ErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(errorCode.getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResDto.of(errorCode)));
        }
    }
}
