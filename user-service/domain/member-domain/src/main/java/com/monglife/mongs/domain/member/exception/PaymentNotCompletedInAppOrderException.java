package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.errorCode.DomainMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class PaymentNotCompletedInAppOrderException extends ErrorException {

    public PaymentNotCompletedInAppOrderException() {
        this.errorCode = DomainMemberErrorCode.PAYMENT_NOT_COMPLETED;
        this.result = Collections.emptyMap();
    }
}
