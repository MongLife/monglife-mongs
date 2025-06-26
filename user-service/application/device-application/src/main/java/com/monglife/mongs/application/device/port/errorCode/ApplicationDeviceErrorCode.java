package com.monglife.mongs.application.device.port.errorCode;

import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationDeviceErrorCode implements ErrorCode {

    NOT_EXISTS_STEP("400-200-000", "걸음 수가 존재하지 않습니다."),
    ;

    private final String code;

    private final String message;
}
