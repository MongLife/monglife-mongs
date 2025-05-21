package com.monglife.mongs.application.battle.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.battle.port.errorCode.ApplicationBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsQueuePlayerException extends ErrorException {

    public NotExistsQueuePlayerException() {
        this.errorCode = ApplicationBattleErrorCode.NOT_EXISTS_QUEUE_PLAYER;
        this.result = Collections.emptyMap();
    }
}
