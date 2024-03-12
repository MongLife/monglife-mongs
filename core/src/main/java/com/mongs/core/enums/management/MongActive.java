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
    CLEAN("AT008", "청소"),
    EVOLUTION("AT009", "진화"),
    GRADUATION("AT010", "졸업"),
    PENALTY("AT011", "패널티"),
    BATTLE("AT012", "배틀"),
    CHARGING("AT013", "충전중"),
    DISCHARGING("AT014", "충전 중지");

    private final String code;
    private final String name;
}