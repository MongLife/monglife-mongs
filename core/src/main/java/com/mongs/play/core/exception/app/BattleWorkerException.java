package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;

public class BattleWorkerException extends AppErrorException {
    public BattleWorkerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BattleWorkerException(Throwable e) {
        super(e);
    }

    public BattleWorkerException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
