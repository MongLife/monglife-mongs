package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsOrderException extends ErrorException {

    public NotExistsOrderException() {
        this.errorCode = ApplicationMemberErrorCode.NOT_EXISTS_ORDER;
        this.result = Collections.emptyMap();
    }
}
