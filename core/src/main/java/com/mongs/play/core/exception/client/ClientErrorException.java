package com.mongs.play.core.exception.client;

import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class ClientErrorException extends ErrorException {
    public ClientErrorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ClientErrorException(Throwable e) {
        super(e);
    }

    public ClientErrorException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
