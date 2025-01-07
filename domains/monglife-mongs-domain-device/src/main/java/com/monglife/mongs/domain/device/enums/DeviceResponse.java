package com.monglife.mongs.domain.device.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum DeviceResponse implements Response {

    /**
     * 실패 응답
     */
    DOMAIN_DEVICE_NOT_ENOUGH_WALKING_COUNT(HttpStatus.BAD_REQUEST.value(), "DOMAIN-DEVICE-100", "충분한 걸음 수가 없습니다."),
    DOMAIN_DEVICE_NOT_EXISTS_STEP(HttpStatus.BAD_REQUEST.value(), "DOMAIN-DEVICE-101", "걸음 수가 존재하지 않습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;

    @Override
    public ResponseDto<Map<String, Object>> toResponseDto() {
        return new ResponseDto<>(code, message, httpStatus, Collections.emptyMap());
    }

    @Override
    public <T> ResponseDto<T> toResponseDto(T result) {
        return new ResponseDto<>(code, message, httpStatus, result);
    }

}
