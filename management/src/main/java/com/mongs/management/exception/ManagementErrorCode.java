package com.mongs.management.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ManagementErrorCode implements ErrorCode {

    SUCCESS(HttpStatus.OK, "200","성공"),
    IMPOSSIBLE(HttpStatus.NOT_ACCEPTABLE, "201","할 수 없음"),
    NULL_POINT(HttpStatus.BAD_REQUEST, "500","빈 값이 있습니다."),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "501", "해당 id를 가진 몽을 찾지 못했습니다."),
    NOT_ACTION(HttpStatus.BAD_REQUEST, "502", "해당 액션을 찾을 수 없습니다."),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "503", "알 수 없음"),
    GATEWAY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "504", "게이트웨이 통신 에러 입니다."),
    ALREADY_EXIST(HttpStatus.NOT_ACCEPTABLE, "505", "이미 몽이 존재합니다."),
    UNSUITABLE(HttpStatus.BAD_REQUEST, "506", "적절하지 않은 몽입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
