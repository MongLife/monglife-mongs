package com.monglife.mongs.domain.device.errorCode;

import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DomainDeviceErrorCode implements ErrorCode {

    INVALID_EXCHANGE_WALKING_COUNT("500-200-003", "환전할 걸음 수가 올바르지 않습니다."),
    EXCEED_DAILY_EXCHANGE_WALKING_COUNT("500-200-004", "하루에 환전할 수 있는 걸음 수를 초과했습니다."),
    ;

    private final String code;

    private final String message;
}
