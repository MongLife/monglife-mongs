package com.monglife.mongs.adapter.in.device.web.enums;

import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdapterInDeviceWebResponse implements Response {

    EXCHANGE_CURRENT_WALKING_COUNT(HttpStatus.OK.value(), "MONGS-USER-DEVICE-000", "걸음수 환전에 성공했습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;
}
