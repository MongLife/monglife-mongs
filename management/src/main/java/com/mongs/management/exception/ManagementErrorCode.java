package com.mongs.management.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ManagementErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MANAGEMENT-100", "내부 서버 에러"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "MANAGEMENT-101", "파라미터가 유효하지 않습니다."),
    INVALID_EVOLUTION(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT-102", "진화 가능한 상태가 아닙니다."),
    INVALID_GRADUATION(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT-103", "졸업 가능한 상태가 아닙니다."),
    INVALID_STROKE(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT-104", "쓰다듬기 가능한 상태가 아닙니다."),
    INVALID_POOP_CLEAN(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT-105", "배변 처리 가능한 상태가 아닙니다."),

    FEIGN_CLIENT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "MANAGEMENT-106", "Feign Client Request Fail"),
    CONNECT_REFUSE(HttpStatus.INTERNAL_SERVER_ERROR, "MANAGEMENT-107", "Mqtt Connect Fail"),
    GENERATE_DATA_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "MANAGEMENT-108", "Generate Data Fail (Json Paring Fail)"),

    NOT_ENOUGH_PAYPOINT(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT-109", "훈련을 위한 충분한 포인트가 없습니다."),
    NOT_ENOUGH_EXP(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT-110", "졸업하기 위한 충분한 경험치가 쌓이지 않았습니다."),
    NOT_FOUND_MONG(HttpStatus.BAD_REQUEST, "MANAGEMENT-111", "해당 id를 가진 몽을 찾지 못했습니다."),
    NOT_FOUND_FOOD_CODE(HttpStatus.BAD_REQUEST, "MANAGEMENT-112", "음식 코드와 일치하는 음식이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
