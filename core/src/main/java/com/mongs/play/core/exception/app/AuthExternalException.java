package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

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
