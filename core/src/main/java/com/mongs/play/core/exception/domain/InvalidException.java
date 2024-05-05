package com.mongs.play.core.exception.domain;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class InvalidException extends ErrorException {
    public InvalidException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidException(Throwable e) {
        super(e);
    }

    public InvalidException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
