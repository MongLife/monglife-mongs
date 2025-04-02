package com.mongs.play.core.exception.common;


import com.mongs.play.core.error.ErrorCode;

public class GenerateException extends CommonErrorException {
    public GenerateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GenerateException(Throwable e) {
        super(e);
    }

    public GenerateException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
