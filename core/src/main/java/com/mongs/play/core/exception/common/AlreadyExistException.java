package com.mongs.play.core.exception.common;


import com.mongs.play.core.error.ErrorCode;

public class AlreadyExistException extends CommonErrorException {
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
