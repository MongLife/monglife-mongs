package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class CommonExternalException extends ErrorException {
    public CommonExternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CommonExternalException(Throwable e) {
        super(e);
    }

    public CommonExternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
