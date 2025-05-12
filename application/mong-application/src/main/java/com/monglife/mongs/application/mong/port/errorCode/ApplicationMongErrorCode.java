package com.monglife.mongs.application.mong.port.errorCode;

import com.monglife.core.dto.response.ResponseDto;
import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ApplicationMongErrorCode implements ErrorCode {

    NOT_EXISTS_MONG("APPLICATION-MONG-000", "몽이 존재하지 않습니다."),
    NOT_EXISTS_FOOD("APPLICATION-MONG-001", "음식이 존재하지 않습니다."),
    NOT_EXISTS_SNACK("APPLICATION-MONG-002", "간식이 존재하지 않습니다."),
    NOT_EXISTS_INVENTORY_ITEM("APPLICATION-MONG-003", "인벤토리 아이템이 존재하지 않습니다."),
    NOT_EXISTS_RANDOM_DRAW_ITEMS("APPLICATION-MONG-004", "랜덤 뽑기 아이템이 존재하지 않습니다."),
    NOT_EXISTS_TRAINING_TYPE("APPLICATION-MONG-005", "훈련 타입 코드가 존재하지 않습니다."),
    INVALID_USE_INVENTORY_ITEM("APPLICATION-MONG-006", "소비가 불가능한 인벤토리 아이템 입니다."),
    INVALID_CREATE_INVENTORY_ITEM("APPLICATION-MONG-007", "인벤토리 아이템 등록에 실패했습니다."),
    INVALID_CREATE_MONG("APPLICATION-MONG-008", "몽 생성에 실패했습니다."),
    INVALID_STROKE_MONG("APPLICATION-MONG-009", "몽 쓰다듬기가 불가능한 상태입니다."),
    INVALID_CREATE_MONG_STROKE_HISTORY("APPLICATION-MONG-010", "몽 쓰다듬기 이력 등록에 실패했습니다."),
    INVALID_DELETE_INVENTORY_ITEM("APPLICATION-MONG-011", "인벤토리 아이템 삭제에 실패했습니다."),
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
