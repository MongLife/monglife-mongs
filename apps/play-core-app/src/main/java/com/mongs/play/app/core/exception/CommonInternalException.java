package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

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
