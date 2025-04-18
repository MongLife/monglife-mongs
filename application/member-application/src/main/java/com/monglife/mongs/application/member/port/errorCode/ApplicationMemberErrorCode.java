package com.monglife.mongs.application.member.port.errorCode;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ApplicationMemberErrorCode implements ErrorCode {

    NOT_EXISTS_PLAYER("APPLICATION-MEMBER-000", "플레이어가 존재하지 않습니다."),
    NOT_EXISTS_EXCHANGE_STAR_POINT_PRODUCT("APPLICATION-MEMBER-001", "스타 포인트 환전 상품이 존재하지 않습니다."),
    NOT_EXISTS_ORDER("APPLICATION-MEMBER-002", "주문 내역이 존재하지 않습니다."),
    NOT_EXISTS_IN_APP_PRODUCT("APPLICATION-MEMBER-003", "인앱 상품이 존재하지 않습니다."),
    NOT_EXISTS_IN_APP_ORDER("APPLICATION-MEMBER-004", "인앱 상품 주문 내역이 존재하지 않습니다."),
    INVALID_CONSUME_IN_APP_ORDER("APPLICATION-MEMBER-005", "인앱 상품 주문 소비를 할 수 없습니다."),
    INVALID_CREATE_COLLECTION_MAP("APPLICATION-MEMBER-006", "컬렉션 맵을 등록하는데 실패했습니다."),
    INVALID_CREATE_COLLECTION_MONG("APPLICATION-MEMBER-007", "컬렉션 몽을 등록하는데 실패했습니다."),
    INVALID_CREATE_ORDER("APPLICATION-MEMBER-008", "주문을 등록하는데 실패했습니다."),
    INVALID_CREATE_PLAYER("APPLICATION-MEMBER-009", "플레이어를 등록하는데 실패했습니다."),
    INVALID_CREATE_FEEDBACK("APPLICATION-MEMBER-010", "오류 신고를 등록하는데 실패했습니다."),
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
