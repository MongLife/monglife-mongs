package com.mongs.play.core.exception.domain;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

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
