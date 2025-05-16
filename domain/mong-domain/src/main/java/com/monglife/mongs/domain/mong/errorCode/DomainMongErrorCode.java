package com.monglife.mongs.domain.mong.errorCode;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum DomainMongErrorCode implements ErrorCode {

    INVALID_FEED_FOOD("DOMAIN-MONG-000", "음식 섭취가 불가능 합니다."),
    INVALID_FEED_SNACK("DOMAIN-MONG-001", "간식 섭취가 불가능 합니다."),
    INVALID_EVOLUTION("DOMAIN-MONG-002", "진화가 불가능한 상태입니다."),
    INVALID_MONG_STATE("DOMAIN-MONG-003", "변경이 불가능한 몽 상태입니다."),
    NOT_ENOUGH_PAY_POINT("DOMAIN-MONG-004", "충분한 페이 포인트가 없습니다."),
    FORBIDDEN_MONG("DOMAIN-MONG-005", "몽에 대한 권한이 없습니다."),
    FORBIDDEN_INVENTORY_ITEM("DOMAIN-MONG-006", "인벤토리 아이템에 대한 권한이 없습니다."),
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
