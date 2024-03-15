package com.mongs.notification.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorResDto;
import com.mongs.notification.exception.NotificationErrorCode;
import com.mongs.notification.exception.NotificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

@RestControllerAdvice
public class NotificationControllerAdvice {
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<Object> notificationExceptionHandler(NotificationException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Object> connectExceptionHandler() {
        ErrorCode errorCode = NotificationErrorCode.CONNECT_REFUSE;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandler() {
        ErrorCode errorCode = NotificationErrorCode.NOTIFICATION_FAIL;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler() {
        ErrorCode errorCode = NotificationErrorCode.NOTIFICATION_FAIL;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
}
