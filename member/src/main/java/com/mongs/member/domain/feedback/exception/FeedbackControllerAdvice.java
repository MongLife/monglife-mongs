package com.mongs.member.domain.feedback.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorResDto;
import com.mongs.member.domain.feedback.controller.FeedbackController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = { FeedbackController.class })
public class FeedbackControllerAdvice {
    @ExceptionHandler(NotFoundFeedbackException.class)
    public ResponseEntity<Object> notFoundFeedbackHandler(NotFoundFeedbackException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandler(RuntimeException e) {
        ErrorCode errorCode = FeedbackErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception e) {
        ErrorCode errorCode = FeedbackErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
}
