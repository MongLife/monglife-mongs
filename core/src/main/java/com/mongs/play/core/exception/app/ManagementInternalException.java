package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class ManagementInternalException extends ErrorException {
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
