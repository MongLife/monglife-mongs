package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsInAppProductException extends ErrorException {

    public NotExistsInAppProductException() {
        this.errorCode = ApplicationMemberErrorCode.NOT_EXISTS_IN_APP_PRODUCT;
        this.result = Collections.emptyMap();
    }
}
