package com.mongs.auth.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class AuthorizationException extends ErrorException {
    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
    public AuthorizationException(Throwable e) {
        super(e);
    }
    public AuthorizationException(ErrorCode errorCodee, Throwable e) {
        super(errorCodee, e);
    }
}
