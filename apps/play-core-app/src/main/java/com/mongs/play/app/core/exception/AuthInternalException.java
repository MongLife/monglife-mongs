package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

public class AuthInternalException extends ErrorException {
    public AuthInternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthInternalException(Throwable e) {
        super(e);
    }

    public AuthInternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
