package com.monglife.mongs.application.battle.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.battle.port.errorCode.ApplicationBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateMatchException extends ErrorException {

    public InvalidCreateMatchException() {
        this.errorCode = ApplicationBattleErrorCode.INVALID_CREATE_MATCH;
        this.result = Collections.emptyMap();
    }
}
