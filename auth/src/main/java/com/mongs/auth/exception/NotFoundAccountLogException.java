package com.mongs.auth.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class NotFoundAccountLogException extends ErrorException {
    public NotFoundAccountLogException(ErrorCode errorCode) {
        super(errorCode);
    }
    public NotFoundAccountLogException(Throwable e) {
        super(e);
    }
    public NotFoundAccountLogException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
