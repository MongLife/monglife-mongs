package com.mongs.play.core.error.module;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FeignErrorCode implements ErrorCode {
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "FEIGN-100", "service unavailable."),
    NOT_ACCEPTABLE_CHANGE(HttpStatus.NOT_ACCEPTABLE, "FEIGN-101", "invalid change."),
    NOT_FOUND_RESOURCE(HttpStatus.NOT_ACCEPTABLE, "FEIGN-102", "not found resource."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
