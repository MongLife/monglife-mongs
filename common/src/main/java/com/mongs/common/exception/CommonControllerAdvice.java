package com.mongs.common.exception;

import com.mongs.core.error.ErrorResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(NotFoundVersionException.class)
    public ResponseEntity<Object> NotFoundVersionExceptionHandler(NotFoundVersionException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(NewestVersionException.class)
    public ResponseEntity<Object> NewestVersionExceptionHandler(NewestVersionException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(ErrorResDto.of(e.errorCode));
    }
}
