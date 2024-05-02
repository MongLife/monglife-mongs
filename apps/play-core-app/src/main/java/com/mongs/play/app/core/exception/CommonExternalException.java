package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

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
