package com.mongs.collection.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class CodeNotFoundException extends ErrorException {
    public CodeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
    public CodeNotFoundException(Throwable e) {
        super(e);
    }
    public CodeNotFoundException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
