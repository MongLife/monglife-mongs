package com.mongs.play.app.core.exception;

import com.mongs.play.app.core.error.ErrorCode;

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
