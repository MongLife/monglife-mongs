package com.mongs.play.core.exception.common;

import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class CommonErrorException extends ErrorException {
    public CommonErrorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CommonErrorException(Throwable e) {
        super(e);
    }

    public CommonErrorException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
