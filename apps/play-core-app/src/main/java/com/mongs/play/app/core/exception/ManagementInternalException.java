package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

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
