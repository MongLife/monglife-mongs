package com.mongs.core.interceptor.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class GenerateException extends ErrorException {
    public GenerateException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GenerateException(Throwable e) {
        super(e);
    }

    public GenerateException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
