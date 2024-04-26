package com.mongs.member.domain.member.controller;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;
import com.mongs.core.error.ErrorResDto;
import com.mongs.member.domain.member.controller.MemberController;
import com.mongs.member.domain.member.exception.MemberErrorCode;
import com.mongs.member.domain.member.exception.NotFoundMemberException;
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
    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Object> errorExceptionHandler(ErrorException e) {
        ErrorCode errorCode = e.errorCode;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandler() {
        ErrorCode errorCode = MemberErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler() {
        ErrorCode errorCode = MemberErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResDto.of(errorCode));
    }
}
