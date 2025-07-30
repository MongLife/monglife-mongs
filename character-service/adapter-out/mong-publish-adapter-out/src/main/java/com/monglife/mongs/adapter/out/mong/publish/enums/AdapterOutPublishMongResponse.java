package com.monglife.mongs.adapter.out.mong.publish.enums;

import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdapterOutPublishMongResponse implements Response {

    MONG_PUBLISH_MONG(HttpStatus.OK.value(), "MONGS-CHARACTER-MONG-500", "몽 정보에 변동이 있습니다."),
    ;

    private final Integer httpStatus;

    private final String code;

    private final String message;
}
