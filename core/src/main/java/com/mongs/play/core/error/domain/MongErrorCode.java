package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MongErrorCode implements ErrorCode {
    NOT_FOUND_MONG(HttpStatus.NOT_FOUND, "MONG-100", "not found mong."),
    INVALID_STROKE(HttpStatus.NOT_ACCEPTABLE, "MONG-101", "invalid stroke."),
    INVALID_TRAINING(HttpStatus.NOT_ACCEPTABLE, "MONG-102", "invalid training."),
    INVALID_GRADUATION(HttpStatus.NOT_ACCEPTABLE, "MONG-103", "invalid graduation."),
    INVALID_EVOLUTION(HttpStatus.NOT_ACCEPTABLE, "MONG-104", "invalid evolution."),
    NOT_ENOUGH_EXP(HttpStatus.NOT_ACCEPTABLE, "MONG-105", "not enough exp.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
