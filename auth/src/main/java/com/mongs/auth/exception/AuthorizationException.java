package com.mongs.auth.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class AuthorizationException extends ErrorException {
    ErrorCode errorCode;

    public AuthorizationException(AuthErrorCode authErrorCode) {
        super(authErrorCode.getMessage());
        this.errorCode = authErrorCode;
    }
    public AuthorizationException(Throwable e) {
        super(e);
    }
    public AuthorizationException(AuthErrorCode authErrorCode, Throwable e) {
        super(authErrorCode.getMessage(), e);
        this.errorCode = authErrorCode;
    }
}
