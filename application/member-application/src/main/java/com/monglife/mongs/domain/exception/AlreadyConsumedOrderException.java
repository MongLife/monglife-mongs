package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyConsumedOrderException extends ErrorException {

    public AlreadyConsumedOrderException() {
        this.errorCode = DomainMemberErrorCode.ALREADY_CONSUMED_ORDER;
        this.result = Collections.emptyMap();
    }
}
