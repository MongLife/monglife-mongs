package com.monglife.mongs.domain.member.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.member.enums.MemberResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsProductOrderException extends ErrorException {

    public NotExistsProductOrderException(Long productOrderId) {
        this.response = MemberResponse.DOMAIN_MEMBER_NOT_EXISTS_PRODUCT_ORDER;
        this.result = Collections.singletonMap("productOrderId", productOrderId);
    }
}
