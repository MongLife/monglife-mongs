package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

public class ManagementExternalException extends ErrorException {
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
