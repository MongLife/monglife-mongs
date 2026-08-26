package com.monglife.mongs.adapter.out.device.publish.enums;

import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdapterOutPublishDeviceResponse implements Response {

    DEVICE_PUBLISH_RESTORE_WALKING_COUNT(HttpStatus.OK.value(), "MONGS-USER-DEVICE-500", "걸음 수 환전이 취소되었습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;
}
