package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    NOT_ENOUGH_STAR_POINT(HttpStatus.NOT_ACCEPTABLE, "MEMBER-101", "not enough star point."),
    ALREADY_MAX_SLOT_COUNT(HttpStatus.NOT_ACCEPTABLE, "MEMBER-102", "already max slot count."),
    ALREADY_MIN_SLOT_COUNT(HttpStatus.NOT_ACCEPTABLE, "MEMBER-103", "already min slot count.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
