package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongMetaHistoryType {

    INCREASE_STROKE_COUNT("쓰다듬기 횟수 증가"),
    RESET_STROKE_COUNT("쓰다듬기 횟수 초기화"),
    INCREASE_TRAINING_COUNT("훈련 횟수 증가"),
    RESET_TRAINING_COUNT("훈련 횟수 초기화"),
    INCREASE_PENALTY("진화 패널티 증가"),
    DEACTIVATE("비활성화"),
    SET_REWARD("진화 리워드 수정"),
    RESET_PENALTY("진화 패널티 초기화"),
    ;

    public final String name;
}
