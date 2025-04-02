package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;

public class AuthInternalException extends AppErrorException {
    public AuthInternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthInternalException(Throwable e) {
        super(e);
    }

    public AuthInternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
