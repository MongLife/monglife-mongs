package com.mongs.core.security.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class PassportIntegrityException extends ErrorException {
    public PassportIntegrityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PassportIntegrityException(Throwable e) {
        super(e);
    }

    public PassportIntegrityException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
