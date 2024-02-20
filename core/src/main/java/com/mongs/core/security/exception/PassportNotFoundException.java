package com.mongs.core.security.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class PassportNotFoundException extends ErrorException {
    public PassportNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
    public PassportNotFoundException(Throwable e) {
        super(e);
    }
    public PassportNotFoundException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
