package com.mongs.play.module.security.exception;

import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

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
