package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;

public class ManagementInternalException extends AppErrorException {
    public ManagementInternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ManagementInternalException(Throwable e) {
        super(e);
    }

    public ManagementInternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
