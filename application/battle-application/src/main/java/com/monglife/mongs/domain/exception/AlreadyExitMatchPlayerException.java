package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyExitMatchPlayerException extends ErrorException {

    public AlreadyExitMatchPlayerException() {
        this.errorCode = DomainBattleErrorCode.ALREADY_EXIT_MATCH_PLAYER;
        this.result = Collections.emptyMap();
    }
}
