package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongActive {

    FEED("AT000", "먹이"),
    BOWEL("AT001", "배변"),
    POOP_CLEAN("AT002", "배변 처리"),
    SLEEP("AT003", "수면"),
    AWAKE("AT004", "기상"),
    TRAINING("AT005", "훈련"),
    STROKE("AT006", "쓰다 듬기"),
    EVOLUTION("AT007", "진화"),
    GRADUATION("AT008", "졸업"),
    PENALTY("AT009", "패널티"),
    BATTLE("AT010", "배틀"),
    CREATE("AT011", "생성"),
    DELETE("AT012", "삭제"),
    DEAD("AT013", "사망")
    ;

    private final String code;
    private final String message;
}