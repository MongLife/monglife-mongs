package com.mongs.play.core.exception.app;

import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class AppErrorException extends ErrorException {
    public AppErrorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AppErrorException(Throwable e) {
        super(e);
    }

    public AppErrorException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
