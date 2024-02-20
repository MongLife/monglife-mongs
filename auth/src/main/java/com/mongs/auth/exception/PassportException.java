package com.mongs.auth.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class PassportException extends ErrorException {
    ErrorCode errorCode;

    public PassportException(AuthErrorCode authErrorCode) {
        super(authErrorCode.getMessage());
        this.errorCode = authErrorCode;
    }
    public PassportException(Throwable e) {
        super(e);
    }
    public PassportException(AuthErrorCode authErrorCode, Throwable e) {
        super(authErrorCode.getMessage(), e);
        this.errorCode = authErrorCode;
    }
}
