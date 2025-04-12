package com.monglife.mongs.global.errorCode;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ApplicationDeviceErrorCode implements ErrorCode {

    NOT_ENOUGH_CURRENT_WALKING_COUNT("APPLICATION-DEVICE-000", "충분한 보유 걸음 수가 없습니다."),
    NOT_EXISTS_STEP("APPLICATION_DEVICE-001", "걸음 수가 존재하지 않습니다.")
    ;

    private final String code;

    private final String message;

    @Override
    public ResponseDto<Map<String, Object>> toResponseDto(Integer httpStatus) {
        return new ResponseDto<>(code, message, httpStatus, Collections.emptyMap());
    }

    @Override
    public <T> ResponseDto<T> toResponseDto(Integer httpStatus, T result) {
        return new ResponseDto<>(code, message, httpStatus, result);
    }
}
