package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.errorCode.DomainMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEnoughPayPointException extends ErrorException {

    public NotEnoughPayPointException() {
        this.errorCode = DomainMongErrorCode.NOT_ENOUGH_PAY_POINT;
        this.result = Collections.emptyMap();
    }
}
