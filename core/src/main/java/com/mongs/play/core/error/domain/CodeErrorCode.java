package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CodeErrorCode implements ErrorCode {
    NOT_FOUND_CODE_VERSION(HttpStatus.NOT_FOUND, "CODE-100", "not found code version."),
    NOT_FOUND_MAP_CODE(HttpStatus.NOT_FOUND, "CODE-101", "not found map code."),
    NOT_FOUND_MONG_CODE(HttpStatus.NOT_FOUND, "CODE-102", "not found mong code."),
    ALREADY_EXIST_MAP_CODE(HttpStatus.BAD_REQUEST, "CODE-103", "already exist map code."),
    ALREADY_EXIST_MONG_CODE(HttpStatus.BAD_REQUEST, "CODE-104", "already exist mong code."),
    ALREADY_EXIST_FOOD_CODE(HttpStatus.BAD_REQUEST, "CODE-105", "already exist food code."),
    ALREADY_EXIST_FEEDBACK_CODE(HttpStatus.BAD_REQUEST, "CODE-106", "already exist feedback code.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
