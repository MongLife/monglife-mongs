package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEnoughPayPointException extends ErrorException {

    public NotEnoughPayPointException() {
        this.errorCode = DomainMongErrorCode.NOT_ENOUGH_PAY_POINT;
        this.result = Collections.emptyMap();
    }
}
