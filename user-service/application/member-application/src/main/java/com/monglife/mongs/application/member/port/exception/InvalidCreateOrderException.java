package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateOrderException extends ErrorException {

    public InvalidCreateOrderException() {
        this.errorCode = ApplicationMemberErrorCode.INVALID_CREATE_ORDER;
        this.result = Collections.emptyMap();
    }
}
