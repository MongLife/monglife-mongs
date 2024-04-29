package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongState {

    SOMNOLENCE("CD001","졸림", 0D, 0D, 0D, 0D, 10D),
    HUNGRY("CD002","배고픔", 0D, 0D, 10D, 0D, 0D),
    SICK("CD003","아픔", 0D, 0D, 0D, 10D, 0D),
    NORMAL("CD000","정상", 0D, 0D, 0D, 0D, 0D),
    EMPTY("CD444","없음", 0D, 0D, 0D, 0D, 0D),
    ;

    private final String code;
    private final String message;
    private final Double weightPercent;
    private final Double strengthPercent;
    private final Double satietyPercent;
    private final Double healthyPercent;
    private final Double sleepPercent;
}