package com.mongs.auth.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class NotFoundAccountException extends ErrorException {
    public NotFoundAccountException(ErrorCode errorCode) {
        super(errorCode);
    }
    public NotFoundAccountException(Throwable e) {
        super(e);
    }
    public NotFoundAccountException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
