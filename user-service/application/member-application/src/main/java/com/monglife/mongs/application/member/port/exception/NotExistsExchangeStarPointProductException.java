package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsExchangeStarPointProductException extends ErrorException {

    public NotExistsExchangeStarPointProductException() {
        this.errorCode = ApplicationMemberErrorCode.NOT_EXISTS_EXCHANGE_STAR_POINT_PRODUCT;
        this.result = Collections.emptyMap();
    }
}
