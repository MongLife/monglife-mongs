package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsInAppOrderException extends ErrorException {

    public NotExistsInAppOrderException() {
        this.errorCode = ApplicationMemberErrorCode.NOT_EXISTS_IN_APP_ORDER;
        this.result = Collections.emptyMap();
    }
}
