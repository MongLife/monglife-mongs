package com.monglife.mongs.domain.member.errorCode;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum DomainMemberErrorCode implements ErrorCode {

    ALREADY_MAX_SLOT_COUNT("500-201-000", "이미 최대 슬롯 수 입니다."),
    NOT_ENOUGH_STAR_POINT("500-201-001", "충분한 스타 포인트가 없습니다."),
    PAYMENT_NOT_COMPLETED("500-201-002", "결제가 완료 되지 않았습니다."),
    ALREADY_CONSUMED_IN_APP_ORDER("500-201-003", "이미 소비된 인앱 주문입니다."),
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
