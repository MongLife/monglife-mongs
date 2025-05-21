package com.monglife.mongs.application.member.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.member.port.errorCode.ApplicationMemberErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidCreatePlayerException extends ErrorException {

    public InvalidCreatePlayerException() {
        this.errorCode = ApplicationMemberErrorCode.INVALID_CREATE_PLAYER;
        this.result = Collections.emptyMap();
    }
}
