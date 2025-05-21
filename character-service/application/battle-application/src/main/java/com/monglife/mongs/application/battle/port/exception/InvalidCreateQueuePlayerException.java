package com.monglife.mongs.application.battle.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.battle.port.errorCode.ApplicationBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateQueuePlayerException extends ErrorException {

    public InvalidCreateQueuePlayerException() {
        this.errorCode = ApplicationBattleErrorCode.INVALID_CREATE_QUEUE_PLAYER;
        this.result = Collections.emptyMap();
    }
}
