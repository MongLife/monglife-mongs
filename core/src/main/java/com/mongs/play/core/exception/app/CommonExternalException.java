package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;

public class CommonExternalException extends AppErrorException {
    public CommonExternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CommonExternalException(Throwable e) {
        super(e);
    }

    public CommonExternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
