package com.monglife.mongs.domain.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.errorCode.DomainMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyConsumeInAppOrderException extends ErrorException {

    public AlreadyConsumeInAppOrderException() {
        this.errorCode = DomainMemberErrorCode.INVALID_CONSUME;
        this.result = Collections.emptyMap();
    }
}
