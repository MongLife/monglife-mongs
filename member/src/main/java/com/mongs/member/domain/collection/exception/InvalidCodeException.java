package com.mongs.member.domain.collection.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class InvalidCodeException extends ErrorException {
    public InvalidCodeException(ErrorCode errorCode) {
        super(errorCode);
    }
    public InvalidCodeException(Throwable e) {
        super(e);
    }
    public InvalidCodeException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
