package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainBattleErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEnoughPayPointException extends ErrorException {

    public NotEnoughPayPointException() {
        this.errorCode = DomainBattleErrorCode.NOT_ENOUGH_PAY_POINT;
        this.result = Collections.emptyMap();
    }
}
