package com.mongs.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongConditionCode implements Code {

    NORMAL("CD000","정상"),
    SICK("CD001","아픔"),
    SLEEP("CD002","수면"),
    SOMNOLENCE("CD003","졸림"),
    HUNGRY("CD004","배고픔"),
    DIE("CD005","죽음"),
    GRADUATE("CD006","졸업"),
    EVOLUTION_READY("CD007","진화대기"),
    EATING("CD008", "먹는중");

    private final String groupCode = "CD";
    private final String code;
    private final String name;
}
