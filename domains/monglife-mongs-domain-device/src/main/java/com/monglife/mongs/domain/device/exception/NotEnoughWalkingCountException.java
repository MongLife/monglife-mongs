package com.monglife.mongs.domain.device.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.device.enums.DeviceResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotEnoughWalkingCountException extends ErrorException {

    public NotEnoughWalkingCountException(Integer walkingCount) {
        this.response = DeviceResponse.DOMAIN_DEVICE_NOT_ENOUGH_WALKING_COUNT;
        this.result = Collections.singletonMap("walkingCount", walkingCount);
    }
}
