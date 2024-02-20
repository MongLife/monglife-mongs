package com.mongs.gateway.exception;

import com.mongs.core.error.ErrorCode;
import com.mongs.core.error.ErrorException;

public class PassportException extends ErrorException {
    ErrorCode errorCode;

    public PassportException(GatewayErrorCode gatewayErrorCode) {
        super(gatewayErrorCode.getMessage());
        this.errorCode = gatewayErrorCode;
    }
    public PassportException(Throwable e) {
        super(e);
    }
    public PassportException(GatewayErrorCode gatewayErrorCode, Throwable e) {
        super(gatewayErrorCode.getMessage(), e);
        this.errorCode = gatewayErrorCode;
    }
}
