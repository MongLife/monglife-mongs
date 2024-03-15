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
    FAILED_SAVE_MONG(HttpStatus.NOT_ACCEPTABLE, "400", "몽 생성하지 못했습니다."),
    NOT_ENOUGH_PAYPOINT(HttpStatus.NOT_ACCEPTABLE, "400", "훈련을 위한 충분한 포인트가 없습니다."),
    NOT_ENOUGH_EXP(HttpStatus.NOT_ACCEPTABLE, "400", "졸업하기 위한 충분한 경험치가 쌓이지 않았습니다."),
    NULL_POINT(HttpStatus.BAD_REQUEST, "400","빈 값이 있습니다."),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "400", "해당 id를 가진 몽을 찾지 못했습니다."),
    NOT_FOUND_FOOD_CODE(HttpStatus.BAD_REQUEST, "400", "음식 코드와 일치하는 음식이 없습니다."),
    NOT_ACTION(HttpStatus.BAD_REQUEST, "502", "해당 액션을 찾을 수 없습니다."),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "503", "알 수 없음"),
    GATEWAY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "504", "게이트웨이 통신 에러 입니다."),
    ALREADY_EXIST(HttpStatus.NOT_ACCEPTABLE, "505", "이미 몽이 존재합니다."),
    UNSUITABLE(HttpStatus.BAD_REQUEST, "506", "적절하지 않은 몽입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MANAGEMENT-100", "Internal Server Error"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "MANAGEMENT-101", "Invalid Parameter"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
