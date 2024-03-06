package com.mongs.member.domain.member.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorResDto;
import com.mongs.member.domain.member.controller.MemberController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackageClasses = {MemberController.class})
public class MemberControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatedExceptionHandler() {
        ErrorCode errorCode = MemberErrorCode.INVALID_PARAMETER;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<Object> notFoundMemberExceptionHandler(NotFoundMemberException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus()).body(ErrorResDto.of(e.errorCode));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> codeNotFoundExceptionHandler() {
        ErrorCode errorCode = MemberErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception e) {
        ErrorCode errorCode = MemberErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
}
