package com.mongs.play.core.error.app;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ManagementWorkerErrorCode implements ErrorCode {
    INVALID_PAUSE_EVENT(HttpStatus.BAD_REQUEST, "MANAGEMENT_WORKER-100", "invalid pause event"),
    INVALID_STOP_EVENT(HttpStatus.BAD_REQUEST, "MANAGEMENT_WORKER-101", "invalid stop event"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
