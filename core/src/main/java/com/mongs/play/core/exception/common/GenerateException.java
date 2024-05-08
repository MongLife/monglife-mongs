package com.mongs.play.core.exception.common;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class GenerateException extends ErrorException {
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
