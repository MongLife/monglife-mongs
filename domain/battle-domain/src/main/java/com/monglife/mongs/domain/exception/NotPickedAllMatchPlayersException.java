package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotPickedAllMatchPlayersException extends ErrorException {

    public NotPickedAllMatchPlayersException() {
        this.errorCode = DomainBattleErrorCode.NOT_PICKED_ALL_MATCH_PLAYERS;
        this.result = Collections.emptyMap();
    }
}
