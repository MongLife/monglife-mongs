package com.mongs.play.app.management.external.controllerAdvice;

import com.mongs.play.app.management.external.controller.ManagementExternalController;
import com.mongs.play.core.dto.res.ErrorResDto;
import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.error.GlobalErrorCode;
import com.mongs.play.core.exception.ErrorException;
import com.mongs.play.core.exception.app.CommonInternalException;
import com.mongs.play.core.exception.app.ManagementExternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ManagementExternalController.class)
public class ManagementExternalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatedExceptionHandler() {
        ErrorCode errorCode = GlobalErrorCode.INVALID_PARAMETER;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }

    @ExceptionHandler(ManagementExternalException.class)
    public ResponseEntity<Object> authExternalExceptionHandler(ManagementExternalException e) {
        ErrorCode errorCode = e.errorCode;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Object> errorExceptionHandler(ErrorException e) {
        ErrorCode errorCode = e.errorCode;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandler(RuntimeException e) {
        ErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler() {
        ErrorCode appErrorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(appErrorCode.getHttpStatus()).body(ErrorResDto.of(appErrorCode));
    }
}
