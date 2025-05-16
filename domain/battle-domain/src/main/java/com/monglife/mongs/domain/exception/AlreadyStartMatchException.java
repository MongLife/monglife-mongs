package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyStartMatchException extends ErrorException {

    public AlreadyStartMatchException() {
        this.errorCode = DomainBattleErrorCode.ALREADY_START_MATCH;
        this.result = Collections.emptyMap();
    }
}
