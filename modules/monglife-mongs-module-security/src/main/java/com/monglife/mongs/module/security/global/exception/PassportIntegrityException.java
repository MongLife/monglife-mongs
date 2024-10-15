package com.monglife.mongs.module.security.global.exception;

import com.monglife.core.enums.error.ErrorCode;
import com.monglife.core.exception.ErrorException;

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
