package com.monglife.mongs.domain.mong.enums;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum MongResponse implements Response {

    /**
     * 실패 응답
     */
    DOMAIN_MONG_NOT_EXISTS_MONG_TYPE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-100", "몽 타입이 존재하지 않습니다."),
    DOMAIN_MONG_NOT_EXISTS_FOOD_TYPE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-101", "음식 타입이 존재하지 않습니다."),
    DOMAIN_MONG_NOT_EXISTS_MONG(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-102", "몽이 존재하지 않습니다."),
    DOMAIN_MONG_NOT_ENOUGH_PAY_POINT(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-103", "충분한 Paypoint 가 없습니다."),
    DOMAIN_MONG_INVALID_MONG_TYPE_LEVEL(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-104", "변경이 불가능한 몽 상태입니다."),
    DOMAIN_MONG_INVALID_FEED(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-105", "현재 섭취가 불가능한 음식입니다."),
    DOMAIN_MONG_INVALID_MONG_STATE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-106", "불가능한 상태 입니다."),
    DOMAIN_MONG_INVALID_GRADUATE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-108", "졸업 불가능한 상태 입니다."),
    DOMAIN_MONG_INVALID_MONG(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-109", "접근할 수 없는 몽입니다."),
    DOMAIN_MONG_NOT_EXISTS_PARAMETER(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DOMAIN-MONG-110", "메서드에 파라미터가 존재하지 않습니다."),
    DOMAIN_MONG_INVALID_STROKE(HttpStatus.BAD_REQUEST.value(), "DOMAIN-MONG-111", "현재 쓰다듬기가 불가능한 상태입니다."),
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
