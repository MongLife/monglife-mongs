package com.mongs.auth.exception;

import com.mongs.auth.dto.response.ErrorResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validatedExceptionHandler(MethodArgumentNotValidException e) {
        var message = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> Objects.requireNonNull(objectError.getCodes())[1])
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(ErrorResDto.builder().message(message).build());
    }
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> authorizationExceptionHandler(AuthorizationException e) {
        var message = e.getMessage();

        return ResponseEntity.badRequest().body(ErrorResDto.builder().message(message).build());
    }
}
