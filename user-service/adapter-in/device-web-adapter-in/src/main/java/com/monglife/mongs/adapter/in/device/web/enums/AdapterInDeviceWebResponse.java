package com.monglife.mongs.adapter.in.device.web.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AdapterInDeviceWebResponse implements Response {

    EXCHANGE_CURRENT_WALKING_COUNT(HttpStatus.OK.value(), "100-200-000", "걸음수 환전에 성공했습니다."),
    UPDATE_TOTAL_WALKING_COUNT(HttpStatus.OK.value(), "100-200-001", "걸음수 동기화에 성공했습니다."),
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
