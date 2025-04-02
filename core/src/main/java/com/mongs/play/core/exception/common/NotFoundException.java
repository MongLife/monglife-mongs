package com.mongs.play.core.exception.common;


import com.mongs.play.core.error.ErrorCode;

public class NotFoundException extends CommonErrorException {
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
