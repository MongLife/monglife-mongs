package com.monglife.mongs.domain.device.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.device.errorCode.DomainDeviceErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidTotalWalkingCountException extends ErrorException {

    public InvalidTotalWalkingCountException() {
        this.errorCode = DomainDeviceErrorCode.INVALID_TOTAL_WALKING_COUNT;
        this.result = Collections.emptyMap();
    }
}
