package com.mongs.play.core.exception.common;


import com.mongs.play.core.error.ErrorCode;

public class InvalidException extends CommonErrorException {
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
