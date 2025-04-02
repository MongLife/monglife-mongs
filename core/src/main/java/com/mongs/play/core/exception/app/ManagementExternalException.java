package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;

public class ManagementExternalException extends AppErrorException {
    public ManagementExternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ManagementExternalException(Throwable e) {
        super(e);
    }

    public ManagementExternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
