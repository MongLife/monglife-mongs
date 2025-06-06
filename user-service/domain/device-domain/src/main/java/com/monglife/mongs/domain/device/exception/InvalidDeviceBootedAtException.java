package com.monglife.mongs.domain.device.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.device.errorCode.DomainDeviceErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidDeviceBootedAtException extends ErrorException {

    public InvalidDeviceBootedAtException() {
        this.errorCode = DomainDeviceErrorCode.INVALID_DEVICE_BOOTED_AT;
        this.result = Collections.emptyMap();
    }
}
