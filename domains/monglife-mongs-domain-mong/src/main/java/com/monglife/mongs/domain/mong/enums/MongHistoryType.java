package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongHistoryType {

    DELETE("삭제"),
    STROKE("쓰다듬기"),
    SLEEP("수면"),
    WAKEUP("기상"),
    POOP_CLEAN("기상"),
    FEED("기상"),
    EVOLUTION_READY("진화 대기"),
    EVOLUTION("진화"),
    GRADUATE_READY("졸업 대기"),
    GRADUATE("졸업"),
    DEAD("죽음"),
    INCREASE_PAY_POINT("페이포인트 증가"),
    DECREASE_PAY_POINT("페이포인트 감소"),
    TRAINING("훈련"),
    ;

    public final String name;
}