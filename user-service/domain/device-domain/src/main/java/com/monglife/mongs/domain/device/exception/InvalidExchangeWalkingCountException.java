package com.monglife.mongs.domain.device.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.device.errorCode.DomainDeviceErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidExchangeWalkingCountException extends ErrorException {

    public InvalidExchangeWalkingCountException() {
        this.errorCode = DomainDeviceErrorCode.INVALID_EXCHANGE_WALKING_COUNT;
        this.result = Collections.emptyMap();
    }
}
