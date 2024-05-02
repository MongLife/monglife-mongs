package com.mongs.play.core.exception.domain;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class InvalidParamException extends ErrorException {
    public InvalidParamException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidParamException(Throwable e) {
        super(e);
    }

    public InvalidParamException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
