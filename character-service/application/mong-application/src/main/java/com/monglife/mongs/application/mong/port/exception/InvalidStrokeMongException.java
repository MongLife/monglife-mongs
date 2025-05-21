package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidStrokeMongException extends ErrorException {

    public InvalidStrokeMongException(Long expirationSeconds) {
        this.errorCode = ApplicationMongErrorCode.INVALID_STROKE_MONG;
        this.result = Collections.singletonMap("expirationSeconds", expirationSeconds);
    }
}
