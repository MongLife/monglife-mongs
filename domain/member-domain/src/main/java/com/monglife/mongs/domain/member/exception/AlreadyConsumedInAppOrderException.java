package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.errorCode.DomainMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyConsumedInAppOrderException extends ErrorException {

    public AlreadyConsumedInAppOrderException() {
        this.errorCode = DomainMemberErrorCode.ALREADY_CONSUMED_IN_APP_ORDER;
        this.result = Collections.emptyMap();
    }
}
