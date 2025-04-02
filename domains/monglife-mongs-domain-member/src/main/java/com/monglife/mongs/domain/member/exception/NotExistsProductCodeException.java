package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsProductCodeException extends ErrorException {

    public NotExistsProductCodeException(String paymentCode) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_PRODUCT_CODE;
        this.result = Collections.singletonMap("paymentCode", paymentCode);
    }
}
