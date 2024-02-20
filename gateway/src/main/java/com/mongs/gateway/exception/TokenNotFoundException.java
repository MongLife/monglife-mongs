package com.mongs.gateway.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class TokenNotFoundException extends ErrorException {
    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
    public TokenNotFoundException(Throwable e) {
        super(e);
    }
    public TokenNotFoundException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
