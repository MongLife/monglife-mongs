package com.monglife.mongs.application.mong.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.mong.port.errorCode.ApplicationMongErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsSnackException extends ErrorException {

    public NotExistsSnackException() {
        this.errorCode = ApplicationMongErrorCode.NOT_EXISTS_SNACK;
        this.result = Collections.emptyMap();
    }
}
