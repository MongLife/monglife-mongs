package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

public class AuthExternalException extends ErrorException {
    public AuthExternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthExternalException(Throwable e) {
        super(e);
    }

    public AuthExternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
