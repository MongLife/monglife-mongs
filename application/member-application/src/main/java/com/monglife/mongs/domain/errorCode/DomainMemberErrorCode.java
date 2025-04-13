package com.monglife.mongs.domain.errorCode;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum DomainMemberErrorCode implements ErrorCode {

    ALREADY_MAX_SLOT_COUNT("DOMAIN-MEMBER-000", "이미 최대 슬롯 수 입니다."),
    NOT_ENOUGH_STAR_POINT("DOMAIN-MEMBER-001", "충분한 스타 포인트가 없습니다."),
    INVALID_CONSUME("DOMAIN-MEMBER-002", "인앱 주문을 소비할 수 없습니다."),
    ALREADY_CONSUME_ORDER("DOMAIN-MEMBER-003", "이미 소비된 주문입니다."),
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
