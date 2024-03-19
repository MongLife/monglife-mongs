package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongState {

    NORMAL("CD000","정상"),
    SICK("CD001","아픔"),
    SOMNOLENCE("CD003","졸림"),
    HUNGRY("CD004","배고픔"),
    EMPTY("CD444","없음")
    ;

    private final String code;
    private final String name;
}