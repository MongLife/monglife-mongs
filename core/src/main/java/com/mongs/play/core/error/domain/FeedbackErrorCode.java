package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FeedbackErrorCode implements ErrorCode {
    NOT_FOUND_FEEDBACK(HttpStatus.NOT_FOUND, "FEEDBACK-100", "not found feedback.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
