package com.mongs.notification.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

@RestControllerAdvice
public class NotificationControllerAdvice {

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Object> jsonProcessingExceptionHandler() {
        ErrorCode errorCode = NotificationErrorCode.NOTIFICATION_FAIL;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<Object> connectExceptionHandler() {
        ErrorCode errorCode = NotificationErrorCode.CONNECT_REFUSE;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
}
