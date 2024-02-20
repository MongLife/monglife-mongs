package com.mongs.gateway.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class PassportException extends ErrorException {
    public PassportException(ErrorCode errorCode) {
        super(errorCode);
    }
    public PassportException(Throwable e) {
        super(e);
    }
    public PassportException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
