package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsPlayerException extends ErrorException {

    public NotExistsPlayerException() {
        this.errorCode = ApplicationMemberErrorCode.NOT_EXISTS_PLAYER;
        this.result = Collections.emptyMap();
    }
}
