package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMatchPlayerException extends ErrorException {

    public NotExistsMatchPlayerException() {
        this.errorCode = DomainBattleErrorCode.NOT_EXISTS_MATCH_PLAYER;
        this.result = Collections.emptyMap();
    }
}
