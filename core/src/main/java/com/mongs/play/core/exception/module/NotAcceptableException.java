package com.mongs.play.core.exception.module;

import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;
import com.mongs.play.core.exception.client.ClientErrorException;

public class NotAcceptableException extends ModuleErrorException {
    public NotAcceptableException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotAcceptableException(Throwable e) {
        super(e);
    }

    public NotAcceptableException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
