package com.monglife.mongs.adapter.out.member.publish.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AdapterOutPublishMemberResponse implements Response {

    MEMBER_PUBLISH_MEMBER_STAR_POINT(HttpStatus.OK.value(), "ADAPTER-OUT-PUBLISH-MEMBER-000", "회원 스타 포인트에 변동이 있습니다."),
    MEMBER_PUBLISH_MEMBER_SLOT_COUNT(HttpStatus.OK.value(), "ADAPTER-OUT-PUBLISH-MEMBER-001", "회원 슬롯 수에 변동이 있습니다."),
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
