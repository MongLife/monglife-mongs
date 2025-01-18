package com.monglife.mongs.client.fcm.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.fcm.enums.FcmResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class InvalidGetDevicesException extends ErrorException {

    public InvalidGetDevicesException() {
        this.response = FcmResponse.CLIENT_USER_GET_DEVICES;
        this.result = Collections.emptyMap();
    }
}
