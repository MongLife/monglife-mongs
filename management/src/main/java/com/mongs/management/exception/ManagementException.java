package com.mongs.management.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class ManagementException extends ErrorException {

    public ManagementException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ManagementException(Throwable e) {
        super(e);
    }

    public ManagementException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
