package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

public class ManagementWorkerException extends ErrorException {
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
