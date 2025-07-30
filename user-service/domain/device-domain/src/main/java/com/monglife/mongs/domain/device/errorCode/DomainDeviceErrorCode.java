package com.monglife.mongs.domain.device.errorCode;

import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DomainDeviceErrorCode implements ErrorCode {

    INVALID_TOTAL_WALKING_COUNT("500-200-000", "총 걸음 수는 현재 총 걸음 수보다 적을 수 없습니다."),
    NOT_ENOUGH_CURRENT_WALKING_COUNT("500-200-001", "충분한 보유 걸음 수가 없습니다."),
    INVALID_DEVICE_BOOTED_AT("500-200-002", "걸음 수 초기화를 위한 기기 부팅 시간이 적절하지 않습니다."),
    ;

    private final String code;

    private final String message;
}
