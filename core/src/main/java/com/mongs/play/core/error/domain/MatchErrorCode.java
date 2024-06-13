package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MatchErrorCode implements ErrorCode {
    NOT_FOUND_MATCH(HttpStatus.NOT_FOUND, "MATCH-100", "not found match."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
