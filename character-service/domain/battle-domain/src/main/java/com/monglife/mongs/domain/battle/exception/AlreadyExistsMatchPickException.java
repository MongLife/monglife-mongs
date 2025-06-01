package com.monglife.mongs.domain.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.battle.errorCode.DomainBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyExistsMatchPickException extends ErrorException {

    public AlreadyExistsMatchPickException() {
        this.errorCode = DomainBattleErrorCode.ALREADY_EXISTS_MATCH_PICK;
        this.result = Collections.emptyMap();
    }
}
