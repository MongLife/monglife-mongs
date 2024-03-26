package com.mongs.member.domain.feedback.exception;

import com.mongs.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FeedbackErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FEEDBACK-100", "Internal Server Error"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "FEEDBACK-101", "Invalid Parameter"),
    NOT_FOUND_FEEDBACK(HttpStatus.BAD_REQUEST, "FEEDBACK-102", "Not Found Feedback")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
