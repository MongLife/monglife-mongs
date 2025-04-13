package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyConsumeOrderException extends ErrorException {

    public AlreadyConsumeOrderException() {
        this.errorCode = DomainMemberErrorCode.ALREADY_CONSUME_ORDER;
        this.result = Collections.emptyMap();
    }
}
