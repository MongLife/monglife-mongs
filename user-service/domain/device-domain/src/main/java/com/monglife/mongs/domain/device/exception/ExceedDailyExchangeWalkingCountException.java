package com.monglife.mongs.domain.device.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.device.errorCode.DomainDeviceErrorCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class ExceedDailyExchangeWalkingCountException extends ErrorException {

    public ExceedDailyExchangeWalkingCountException() {
        this.errorCode = DomainDeviceErrorCode.EXCEED_DAILY_EXCHANGE_WALKING_COUNT;
        this.result = Collections.emptyMap();
    }
}
