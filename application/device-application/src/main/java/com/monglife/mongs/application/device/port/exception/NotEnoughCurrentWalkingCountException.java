package com.monglife.mongs.application.device.port.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.application.device.port.errorCode.ApplicationDeviceErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEnoughCurrentWalkingCountException extends ErrorException {

    public NotEnoughCurrentWalkingCountException() {
        this.errorCode = ApplicationDeviceErrorCode.NOT_ENOUGH_CURRENT_WALKING_COUNT;
        this.result = Collections.emptyMap();
    }
}
