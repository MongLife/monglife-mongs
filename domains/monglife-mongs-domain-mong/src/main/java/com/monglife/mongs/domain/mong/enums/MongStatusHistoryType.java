package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongStatusHistoryType {

    PATCH_STATUS("지수 갱신"),
    INCREASE_EXP("경험치 증가"),
    DECREASE_EXP("경험치 감소"),
    RESET_EXP("경험치 초기화"),
    INCREASE_POOP_COUNT("배변 수 증가"),
    DECREASE_POOP_COUNT("배변 수 감소"),
    RESET_POOP_COUNT("배변 수 초기화"),
    INCREASE_WEIGHT("몸무게 증가"),
    DECREASE_WEIGHT("몸무게 감소"),
    INCREASE_STATUS("지수 증가"),
    DECREASE_STATUS("지수 감소"),
    INCREASE_STATUS_RATIO("지수 비율 증가"),
    DECREASE_STATUS_RATIO("지수 비율 감소"),
    SET_MAX_STATUS("최대 지수 값 변경"),
    SET_CODE("지수 코드 값 변경")
    ;

    public final String name;
}