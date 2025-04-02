package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;

public class PlayerInternalException extends AppErrorException {
    public PlayerInternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PlayerInternalException(Throwable e) {
        super(e);
    }

    public PlayerInternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
