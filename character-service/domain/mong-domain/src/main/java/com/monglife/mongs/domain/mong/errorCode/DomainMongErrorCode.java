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

    INVALID_FEED_FOOD("500-101-000", "음식 섭취가 불가능 합니다."),
    INVALID_FEED_SNACK("500-101-001", "간식 섭취가 불가능 합니다."),
    INVALID_EVOLUTION("500-101-002", "진화가 불가능한 상태입니다."),
    INVALID_MONG_STATE("500-101-003", "변경이 불가능한 몽 상태입니다."),
    NOT_ENOUGH_PAY_POINT("500-101-004", "충분한 페이 포인트가 없습니다."),
    FORBIDDEN_MONG("500-101-005", "몽에 대한 권한이 없습니다."),
    FORBIDDEN_INVENTORY_ITEM("500-101-006", "인벤토리 아이템에 대한 권한이 없습니다."),
    NOT_ENOUGH_RANDOM_DRAW_TICKET("500-101-007", "충분한 랜덤 뽑기 티켓이 없습니다."),
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
