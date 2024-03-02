package com.mongs.common.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class NewestVersionException extends ErrorException {
    public NewestVersionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NewestVersionException(Throwable e) {
        super(e);
    }

    public NewestVersionException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
