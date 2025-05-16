package com.monglife.mongs.domain.device.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.device.errorCode.DomainDeviceErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEnoughCurrentWalkingCountException extends ErrorException {

    public NotEnoughCurrentWalkingCountException() {
        this.errorCode = DomainDeviceErrorCode.NOT_ENOUGH_CURRENT_WALKING_COUNT;
        this.result = Collections.emptyMap();
    }
}
