package com.monglife.mongs.global.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.global.errorCode.ApplicationDeviceErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistStepException extends ErrorException {

    public NotExistStepException() {
        this.errorCode = ApplicationDeviceErrorCode.NOT_EXISTS_STEP;
        this.result = Collections.emptyMap();
    }
}
