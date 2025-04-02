package com.monglife.mongs.domain.device.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.device.enums.DeviceResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsDeviceException extends ErrorException {

    public NotExistsDeviceException(String deviceId) {
        this.response = DeviceResponse.DOMAIN_DEVICE_NOT_EXISTS_DEVICE;
        this.result = Collections.singletonMap("deviceId", deviceId);
    }
}
