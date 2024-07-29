package com.mongs.play.core.error.app;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BattleWorkerErrorCode implements ErrorCode {
    NOT_MATCH_MONG(HttpStatus.FORBIDDEN, "BATTLE-100", "mong forbidden error."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
