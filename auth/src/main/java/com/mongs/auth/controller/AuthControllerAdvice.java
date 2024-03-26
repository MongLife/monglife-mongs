package com.mongs.auth.controller;

import com.mongs.auth.exception.AuthErrorCode;
import com.mongs.auth.exception.AuthorizationException;
import com.mongs.auth.exception.NotFoundException;
import com.mongs.auth.exception.PassportException;
import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> authorizationExceptionHandler(AuthorizationException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundExceptionHandler(NotFoundException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(PassportException.class)
    public ResponseEntity<Object> passportExceptionHandler(PassportException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatedExceptionHandler() {
        ErrorCode errorCode = AuthErrorCode.INVALID_PARAMETER;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandler() {
        ErrorCode errorCode = AuthErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler() {
        ErrorCode errorCode = AuthErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
}
