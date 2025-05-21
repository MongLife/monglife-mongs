package com.monglife.mongs.application.battle.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.battle.port.errorCode.ApplicationBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongException extends ErrorException {

    public NotExistsMongException() {
        this.errorCode = ApplicationBattleErrorCode.NOT_EXISTS_MONG;
        this.result = Collections.emptyMap();
    }
}
