package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;

public class ManagementWorkerException extends AppErrorException {
    public ManagementWorkerException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ManagementWorkerException(Throwable e) {
        super(e);
    }

    public ManagementWorkerException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
