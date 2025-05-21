package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsFoodException extends ErrorException {

    public NotExistsFoodException() {
        this.errorCode = ApplicationMongErrorCode.NOT_EXISTS_FOOD;
        this.result = Collections.emptyMap();
    }
}
