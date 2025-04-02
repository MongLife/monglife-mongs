package com.mongs.play.core.error.app;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ManagementExternalErrorCode implements ErrorCode {
    NOT_MATCH_MONG(HttpStatus.FORBIDDEN, "MANAGEMENT_EXTERNAL-101", "mong forbidden error."),
    INVALID_REGISTER(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-107", "invalid register."),
    INVALID_STROKE(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-102", "invalid stroke."),
    INVALID_SLEEPING(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-103", "invalid sleeping."),
    INVALID_AWAKE(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-104", "invalid wake up."),
    INVALID_POOP_CLEAN(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-105", "invalid poop clean."),
    INVALID_TRAINING(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-106", "invalid training."),
    //    INVALID_TRAINING_REWARD(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-107", "invalid training reward."),
    INVALID_GRADUATION(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-108", "invalid graduation."),
    INVALID_EVOLUTION(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-109", "invalid evolution."),
    INVALID_FEED(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-110", "invalid feed."),
    NOT_ENOUGH_EXP(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-111", "not enough exp."),
    NOT_ENOUGH_PAY_POINT(HttpStatus.NOT_ACCEPTABLE, "MANAGEMENT_EXTERNAL-112", "not enough pay point.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
