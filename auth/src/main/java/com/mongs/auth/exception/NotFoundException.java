package com.mongs.auth.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class NotFoundException extends ErrorException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
    public NotFoundException(Throwable e) {
        super(e);
    }
    public NotFoundException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
