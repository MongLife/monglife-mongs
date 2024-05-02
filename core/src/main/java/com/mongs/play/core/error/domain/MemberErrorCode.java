package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "MEMBER-100", "not found member.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
