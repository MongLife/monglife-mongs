package com.mongs.play.core.error.domain;

import com.mongs.play.core.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BattleErrorCode implements ErrorCode {
    NOT_FOUND_BATTLE(HttpStatus.NOT_FOUND, "BATTLE-100", "not found battle room."),
    NOT_FOUND_PLAYER(HttpStatus.NOT_FOUND, "BATTLE-101", "not found battle player."),
    INVALID_ROUND(HttpStatus.NOT_ACCEPTABLE, "BATTLE-102", "invalid round."),
    INVALID_BATTLE_ENTER(HttpStatus.NOT_ACCEPTABLE, "BATTLE-103", "invalid enter battle room."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
