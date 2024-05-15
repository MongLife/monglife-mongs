package com.mongs.play.core.error.app;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ManagementExternalErrorCode implements ErrorCode {
    INVALID_CHANGE_MONG(HttpStatus.FORBIDDEN, "MANAGEMENT_EXTERNAL-100", "invalid change mong."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
