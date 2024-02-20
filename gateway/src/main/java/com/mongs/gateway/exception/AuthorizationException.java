package com.mongs.gateway.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class AuthorizationException extends ErrorException {

    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
    public AuthorizationException(Throwable e) {
        super(e);
    }
    public AuthorizationException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
