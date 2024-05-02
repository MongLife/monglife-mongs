package com.mongs.play.core.exception.app;


import com.mongs.play.core.error.ErrorCode;
import com.mongs.play.core.exception.ErrorException;

public class GatewayExternalException extends ErrorException {
    public GatewayExternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GatewayExternalException(Throwable e) {
        super(e);
    }

    public GatewayExternalException(ErrorCode errorCode, Throwable e) {
        super(errorCode, e);
    }
}
