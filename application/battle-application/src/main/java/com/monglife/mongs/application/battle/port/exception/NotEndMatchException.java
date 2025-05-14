package com.monglife.mongs.application.battle.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.battle.port.errorCode.ApplicationBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEndMatchException extends ErrorException {

    public NotEndMatchException() {
        this.errorCode = ApplicationBattleErrorCode.NOT_END_MATCH;
        this.result = Collections.emptyMap();
    }
}
