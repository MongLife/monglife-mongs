package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongActive {

    MEAL("AT000", "식사"),
    SNACK("AT001", "간식"),
    BOWEL("AT002", "배변"),
    SLEEP("AT003", "수면"),
    WALKING("AT004", "산책"),
    TRAINING("AT005", "훈련"),
    STROKE("AT006", "쓰다듬기"),
    AWAKE("AT007", "기상"),
    EVOLUTION("AT008", "진화"),
    GRADUATION("AT009", "졸업"),
    PENALTY("AT010", "패널티"),
    BATTLE("AT011", "배틀"),
    ;

    private final String code;
    private final String name;
}