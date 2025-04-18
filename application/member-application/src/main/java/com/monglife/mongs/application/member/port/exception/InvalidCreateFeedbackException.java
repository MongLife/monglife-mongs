package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreateFeedbackException extends ErrorException {

    public InvalidCreateFeedbackException() {
        this.errorCode = ApplicationMemberErrorCode.INVALID_CREATE_FEEDBACK;
        this.result = Collections.emptyMap();
    }
}
