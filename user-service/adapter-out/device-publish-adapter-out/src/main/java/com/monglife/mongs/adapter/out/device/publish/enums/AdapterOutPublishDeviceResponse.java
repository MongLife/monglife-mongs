package com.monglife.mongs.adapter.out.device.publish.enums;

import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdapterOutPublishDeviceResponse implements Response {

    DEVICE_PUBLISH_DEVICE(HttpStatus.OK.value(), "MONGS-USER-DEVICE-500", "기기 정보에 변동이 있습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;
}
