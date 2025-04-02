package com.mongs.play.app.player.external.collection.controllerAdvice;

import com.mongs.play.app.player.external.collection.controller.PlayerExternalCollectionController;
import com.mongs.play.core.dto.res.ErrorResDto;
import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.error.GlobalErrorCode;
import com.mongs.play.core.exception.ErrorException;
import com.mongs.play.core.exception.app.PlayerExternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = PlayerExternalCollectionController.class)
public class PlayerExternalCollectionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatedExceptionHandler() {
        ErrorCode errorCode = GlobalErrorCode.INVALID_PARAMETER;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }

    @ExceptionHandler(PlayerExternalException.class)
    public ResponseEntity<Object> authExternalExceptionHandler(PlayerExternalException e) {
        ErrorCode errorCode = e.errorCode;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Object> errorExceptionHandler(ErrorException e) {
        ErrorCode errorCode = e.errorCode;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandler() {
        ErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler() {
        ErrorCode appErrorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(appErrorCode.getHttpStatus()).body(ErrorResDto.of(appErrorCode));
    }
}
