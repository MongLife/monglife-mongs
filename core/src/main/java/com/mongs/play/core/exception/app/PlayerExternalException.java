package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class PlayerExternalException extends ErrorException {
    public PlayerExternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PlayerExternalException(Throwable e) {
        super(e);
    }

    public PlayerExternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
