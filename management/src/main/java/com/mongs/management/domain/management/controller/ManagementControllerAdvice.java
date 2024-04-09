package com.mongs.management.domain.management.controller;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorResDto;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.exception.ManagementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

@RestControllerAdvice(basePackageClasses = { ManagementControllerAdvice.class })
public class ManagementControllerAdvice {
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Object> connectExceptionHandler() {
        ErrorCode errorCode = ManagementErrorCode.CONNECT_REFUSE;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(ManagementException.class)
    public ResponseEntity<Object> managementExceptionHandler(ManagementException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatedExceptionHandler() {
        ErrorCode errorCode = ManagementErrorCode.INVALID_PARAMETER;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandler() {
        ErrorCode errorCode = ManagementErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler() {
        ErrorCode errorCode = ManagementErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
}
