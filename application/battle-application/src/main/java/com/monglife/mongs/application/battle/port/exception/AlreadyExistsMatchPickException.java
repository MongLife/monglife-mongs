package com.monglife.mongs.application.battle.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.battle.port.errorCode.ApplicationBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyExistsMatchPickException extends ErrorException {

    public AlreadyExistsMatchPickException() {
        this.errorCode = ApplicationBattleErrorCode.ALREADY_EXISTS_MATCH_PICK;
        this.result = Collections.emptyMap();
    }
}
