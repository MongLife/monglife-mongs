package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsNoticeException extends ErrorException {

    public NotExistsNoticeException() {
        this.errorCode = ApplicationMemberErrorCode.NOT_EXISTS_NOTICE;
        this.result = Collections.emptyMap();
    }
}
