package com.mongs.lifecycle.controller;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorResDto;
import com.mongs.lifecycle.exception.EventTaskException;
import com.mongs.lifecycle.exception.LifecycleErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

@RestControllerAdvice
public class LifecycleControllerAdvice {
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Object> connectExceptionHandler() {
        ErrorCode errorCode = LifecycleErrorCode.CONNECT_REFUSE;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(EventTaskException.class)
    public ResponseEntity<Object> eventTaskExceptionHandler(EventTaskException e) {
        ErrorCode errorCode = e.errorCode;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandler(RuntimeException e) {
        ErrorCode errorCode = LifecycleErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception e) {
        ErrorCode errorCode = LifecycleErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
}
