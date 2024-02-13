package com.mongs.auth.exception;

import com.mongs.auth.dto.response.ErrorResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatedExceptionHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ErrorResDto.builder()
                .code(ErrorCode.INVALID_PARAMETER.getCode())
                .message(ErrorCode.INVALID_PARAMETER.getMessage())
                .build());
    }
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> authorizationExceptionHandler(AuthorizationException e) {
        return ResponseEntity.badRequest().body(ErrorResDto.builder()
                .code(ErrorCode.REFRESH_TOKEN_EXPIRED.getCode())
                .message(ErrorCode.REFRESH_TOKEN_EXPIRED.getMessage())
                .build());
    }
}
