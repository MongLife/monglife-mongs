package com.mongs.play.domain.mong.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MongLogCode {

    CREATE("AT001", "생성"),
    DELETE("AT002", "삭제"),
    STROKE("AT003", "쓰다 듬기"),
    SLEEP("AT004", "수면"),
    AWAKE("AT005", "기상"),
    BOWEL("AT006", "배변"),
    POOP_CLEAN("AT007", "배변 처리"),
    TRAINING("AT008", "훈련"),
    GRADUATE("AT009", "졸업"),
    EVOLUTION("AT010", "진화"),
    EVOLUTION_READY("AT011", "진화 준비"),
    FEED("AT012", "식사"),
    PENALTY("AT013", "패널티"),
    DEAD("AT014", "사망"),
    BATTLE("AT015", "배틀"),

    DECREASE_WEIGHT("AT100", "몸무게 감소"),
    DECREASE_STRENGTH("AT101", "힘 감소"),
    DECREASE_SATIETY("AT102", "포만감 감소"),
    DECREASE_HEALTHY("AT103", "체력 감소"),
    DECREASE_SLEEP("AT104", "피로도 감소"),

    INCREASE_WEIGHT("AT200", "몸무게 증가"),
    INCREASE_STRENGTH("AT201", "힘 증가"),
    INCREASE_SATIETY("AT202", "포만감 증가"),
    INCREASE_HEALTHY("AT203", "체력 증가"),
    INCREASE_SLEEP("AT204", "피로도 증가"),
    ;

    public final String code;
    public final String message;
}