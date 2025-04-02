package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;

public class CommonInternalException extends AppErrorException {
    public CommonInternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CommonInternalException(Throwable e) {
        super(e);
    }

    public CommonInternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
