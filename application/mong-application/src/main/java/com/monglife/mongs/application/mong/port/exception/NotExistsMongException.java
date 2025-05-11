package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongException extends ErrorException {

    public NotExistsMongException() {
        this.errorCode = ApplicationMongErrorCode.NOT_EXISTS_MONG;
        this.result = Collections.emptyMap();
    }
}
