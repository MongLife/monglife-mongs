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
    INVALID_SLEEPING(HttpStatus.NOT_ACCEPTABLE, "MONG-102", "invalid sleeping."),
    INVALID_AWAKE(HttpStatus.NOT_ACCEPTABLE, "MONG-103", "invalid wake up."),
    INVALID_POOP_CLEAN(HttpStatus.NOT_ACCEPTABLE, "MONG-104", "invalid poop clean."),
    INVALID_TRAINING(HttpStatus.NOT_ACCEPTABLE, "MONG-105", "invalid training."),
    INVALID_GRADUATION(HttpStatus.NOT_ACCEPTABLE, "MONG-106", "invalid graduation."),
    INVALID_EVOLUTION(HttpStatus.NOT_ACCEPTABLE, "MONG-107", "invalid evolution."),
    INVALID_FEED(HttpStatus.NOT_ACCEPTABLE, "MONG-108", "invalid feed."),
    NOT_ENOUGH_EXP(HttpStatus.NOT_ACCEPTABLE, "MONG-109", "not enough exp."),
    NOT_FOUND_TRAINING_CODE(HttpStatus.NOT_FOUND, "MONG-100", "not found training code.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
