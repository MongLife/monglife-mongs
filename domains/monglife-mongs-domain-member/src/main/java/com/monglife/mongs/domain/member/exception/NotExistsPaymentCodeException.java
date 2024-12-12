package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsPaymentCodeException extends ErrorException {

    public NotExistsPaymentCodeException(String paymentCode) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_PAYMENT_CODE;
        this.result = Collections.singletonMap("paymentCode", paymentCode);
    }
}
