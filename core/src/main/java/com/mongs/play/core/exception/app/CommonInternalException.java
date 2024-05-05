package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class CommonInternalException extends ErrorException {
    public CommonInternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CommonInternalException(Throwable e) {
        super(e);
    }

    public CommonInternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
