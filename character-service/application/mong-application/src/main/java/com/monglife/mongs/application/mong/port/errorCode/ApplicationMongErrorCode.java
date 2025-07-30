package com.monglife.mongs.application.mong.port.errorCode;

import com.monglife.core.enums.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationMongErrorCode implements ErrorCode {

    NOT_EXISTS_MONG("400-101-000", "몽이 존재하지 않습니다."),
    NOT_EXISTS_FOOD("400-101-001", "음식이 존재하지 않습니다."),
    NOT_EXISTS_SNACK("400-101-002", "간식이 존재하지 않습니다."),
    NOT_EXISTS_INVENTORY_ITEM("400-101-003", "인벤토리 아이템이 존재하지 않습니다."),
    NOT_EXISTS_RANDOM_DRAW_ITEMS("400-101-004", "랜덤 뽑기 아이템이 존재하지 않습니다."),
    NOT_EXISTS_TRAINING_TYPE("400-101-005", "훈련 타입 코드가 존재하지 않습니다."),
    INVALID_USE_INVENTORY_ITEM("400-101-006", "소비가 불가능한 인벤토리 아이템 입니다."),
    INVALID_CREATE_INVENTORY_ITEM("400-101-007", "인벤토리 아이템 등록에 실패했습니다."),
    INVALID_CREATE_MONG("400-101-008", "몽 생성에 실패했습니다."),
    INVALID_STROKE_MONG("400-101-009", "몽 쓰다듬기가 불가능한 상태입니다."),
    INVALID_CREATE_MONG_STROKE_HISTORY("400-101-010", "몽 쓰다듬기 이력 등록에 실패했습니다."),
    INVALID_CREATE_MONG_FEED_HISTORY("400-101-011", "몽 섭취 이력 등록에 실패했습니다."),
    INVALID_DELETE_INVENTORY_ITEM("400-101-012", "인벤토리 아이템 삭제에 실패했습니다."),
    INVALID_CREATE_MONG_SCHEDULE("400-101-013", "몽 스케줄 등록에 실패했습니다."),
    ;

    private final String code;

    private final String message;
}
