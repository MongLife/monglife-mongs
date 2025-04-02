package com.mongs.play.core.exception.common;


import com.mongs.play.core.error.ErrorCode;

public class InvalidParamException extends CommonErrorException {
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
