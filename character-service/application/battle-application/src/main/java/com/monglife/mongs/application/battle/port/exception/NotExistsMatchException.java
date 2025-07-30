package com.monglife.mongs.application.battle.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.battle.port.errorCode.ApplicationBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMatchException extends ErrorException {

    public NotExistsMatchException() {
        this.errorCode = ApplicationBattleErrorCode.NOT_EXISTS_MATCH;
        this.result = Collections.emptyMap();
    }
}
