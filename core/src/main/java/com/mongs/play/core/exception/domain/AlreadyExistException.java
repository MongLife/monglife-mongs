package com.mongs.play.core.exception.domain;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class AlreadyExistException extends ErrorException {
    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AlreadyExistException(Throwable e) {
        super(e);
    }

    public AlreadyExistException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
