package com.mongs.auth.exception;

import com.mongs.core.error.ErrorResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatedExceptionHandler() {
        return ResponseEntity.badRequest().body(ErrorResDto.of(AuthErrorCode.INVALID_PARAMETER));
    }
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> authorizationExceptionHandler(AuthorizationException e) {
        return ResponseEntity.badRequest().body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundExceptionHandler(NotFoundException e) {
        return ResponseEntity.badRequest().body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(PassportException.class)
    public ResponseEntity<Object> passportExceptionHandler(PassportException e) {
        return ResponseEntity.badRequest().body(ErrorResDto.of(e.errorCode));
    }
}
