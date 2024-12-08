package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongMetaHistoryCode {

    HISTORY_MONG_META_INCREASE_STROKE_COUNT("쓰다듬기 횟수 증가"),
    HISTORY_MONG_META_INCREASE_TRAINING_COUNT("훈련 횟수 증가"),
    HISTORY_MONG_META_INCREASE_PENALTY("진화 패널티 증가"),
    HISTORY_MONG_META_DEACTIVATE("비활성화"),
    HISTORY_MONG_META_SET_REWARD("진화 리워드 수정"),
    HISTORY_MONG_META_RESET_PENALTY("진화 패널티 초기화"),
    ;

    public final String name;
}