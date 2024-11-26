package com.monglife.mongs.app.manager.global.exception;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.exception.ErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ManagerExceptionHandler {

    /**
     * exception handler
     * @param e 예외 객체
     * @return 에러 응답 객체
     */
    @ExceptionHandler(ErrorException.class)
    private ResponseEntity<ResponseDto<Map<String, Object>>> handleErrorException(ErrorException e) {
        return ResponseEntity
                .status(e.getResponse().getHttpStatus())
                .body(e.getResponse().toResponseDto(e.getResult()));
    }
}
