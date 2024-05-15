package com.mongs.play.domain.mong.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MongLogCode {

    CREATE("AT001", "생성"),
    DELETE("AT002", "삭제"),
    STROKE("AT003", "쓰다 듬기"),
    SLEEP("AT004", "수면"),
    AWAKE("AT005", "기상"),
    TRAINING("AT006", "훈련"),
    GRADUATE("AT007", "졸업"),
    EVOLUTION("AT008", "진화"),
    EVOLUTION_READY("AT009", "진화 준비"),
    FEED("AT010", "식사"),
    PENALTY("AT011", "패널티"),
    DEAD("AT012", "사망"),
    BATTLE("AT013", "배틀"),

    DECREASE_STATUS("AT100", "지수 감소"),
    DECREASE_PAY_POINT("AT101", "페이 포인트 감소"),
    DECREASE_POOP_COUNT("AT102", "배변 감소"),

    INCREASE_STATUS("AT200", "지수 증가"),
    INCREASE_PAY_POINT("AT201", "페이 포인트 증가"),
    INCREASE_POOP_COUNT("AT202", "배변 증가"),
    ;

    public final String code;
    public final String message;
}