package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongHistoryCode {

    HISTORY_MONG_DELETE("삭제"),
    HISTORY_MONG_STROKE("쓰다듬기"),
    HISTORY_MONG_SLEEP("수면"),
    HISTORY_MONG_WAKEUP("기상"),
    HISTORY_MONG_POOP_CLEAN("기상"),
    HISTORY_MONG_FEED("기상"),
    HISTORY_MONG_EVOLUTION_READY("진화 대기"),
    HISTORY_MONG_EVOLUTION("진화"),
    HISTORY_MONG_GRADUATE_READY("졸업 대기"),
    HISTORY_MONG_GRADUATE("졸업"),
    HISTORY_MONG_DEAD("죽음"),
    ;

    public final String name;
}