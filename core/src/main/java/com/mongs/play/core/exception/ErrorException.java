package com.mongs.play.core.exception;

import com.mongs.play.core.error.ErrorCode;

public class ErrorException extends RuntimeException {

    public ErrorCode errorCode;

    public ErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorException(Throwable e) {
        super(e);
    }
    public ErrorException(ErrorCode errorCode, Throwable e) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }
}
