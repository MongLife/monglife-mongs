package com.mongs.gateway.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class TokenNotFoundException extends ErrorException {
    ErrorCode errorCode;

    public TokenNotFoundException(GatewayErrorCode gatewayErrorCode) {
        super(gatewayErrorCode.getMessage());
        this.errorCode = gatewayErrorCode;
    }
    public TokenNotFoundException(Throwable e) {
        super(e);
    }
    public TokenNotFoundException(GatewayErrorCode gatewayErrorCode, Throwable e) {
        super(gatewayErrorCode.getMessage(), e);
        this.errorCode = gatewayErrorCode;
    }
}
