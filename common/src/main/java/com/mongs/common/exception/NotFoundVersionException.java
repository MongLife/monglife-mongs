package com.mongs.common.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class NotFoundVersionException extends ErrorException {
    public NotFoundVersionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundVersionException(Throwable e) {
        super(e);
    }

    public NotFoundVersionException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
