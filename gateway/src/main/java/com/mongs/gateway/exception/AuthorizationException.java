package com.mongs.gateway.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class AuthorizationException extends ErrorException {
    ErrorCode errorCode;

    public AuthorizationException(GatewayErrorCode gatewayErrorCode) {
        super(gatewayErrorCode.getMessage());
        this.errorCode = gatewayErrorCode;
    }
    public AuthorizationException(Throwable e) {
        super(e);
    }
    public AuthorizationException(GatewayErrorCode gatewayErrorCode, Throwable e) {
        super(gatewayErrorCode.getMessage(), e);
        this.errorCode = gatewayErrorCode;
    }
}
