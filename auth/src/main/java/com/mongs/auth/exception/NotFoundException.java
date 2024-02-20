package com.mongs.auth.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class NotFoundException extends ErrorException {
    ErrorCode errorCode;

    public NotFoundException(AuthErrorCode authErrorCode) {
        super(authErrorCode.getMessage());
        this.errorCode = authErrorCode;
    }
    public NotFoundException(Throwable e) {
        super(e);
    }
    public NotFoundException(AuthErrorCode authErrorCode, Throwable e) {
        super(authErrorCode.getMessage(), e);
        this.errorCode = authErrorCode;
    }
}
