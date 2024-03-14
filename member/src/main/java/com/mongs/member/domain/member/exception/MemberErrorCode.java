package com.mongs.member.domain.member.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-100", "Not Found Member"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "MEMBER-101", "Invalid Parameter"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER-102", "Internal Server Error")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
