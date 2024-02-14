package com.mongs.auth.exception;

import com.mongs.auth.dto.response.ErrorResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatedExceptionHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ErrorResDto.builder()
                .message(e.getMessage())
                .build());
    }
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> authorizationExceptionHandler(AuthorizationException e) {
        return ResponseEntity.badRequest().body(ErrorResDto.builder()
                .message(e.getMessage())
                .build());
    }
    @ExceptionHandler(PassportException.class)
    public ResponseEntity<Object> passportExceptionHandler(AuthorizationException e) {
        return ResponseEntity.badRequest().body(ErrorResDto.builder()
                .message(e.getMessage())
                .build());
    }
}