package com.mongs.lifecycle.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LifecycleControllerAdvice {
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
