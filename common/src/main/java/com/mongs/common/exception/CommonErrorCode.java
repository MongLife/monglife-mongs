package com.mongs.common.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    NOT_FOUND_VERSION(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-100", "버전 정보를 찾을 수 없습니다."),
    ALREADY_NEW_VERSION(HttpStatus.ACCEPTED, "COMMON-000", "이미 최신 버전입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
