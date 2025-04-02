package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MongErrorCode implements ErrorCode {
    NOT_FOUND_ACTIVE_MONG(HttpStatus.NOT_FOUND, "MONG-100", "not found active mong."),
//    NOT_FOUND_MONG(HttpStatus.NOT_FOUND, "MONG-100", "not found mong."),
    NOT_FOUND_TRAINING_CODE(HttpStatus.NOT_FOUND, "MONG-101", "not found training code."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
