package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongState {

    EMPTY("CD444","없음", -1D, -1D, -1D, -1D, -1D),
    NORMAL("CD000","정상", 100D, 100D, 100D, 100D, 100D),
    SOMNOLENCE("CD001","졸림", 100D, 100D, 100D, 100D, 10D),
    HUNGRY("CD002","배고픔", 100D, 100D, 10D, 100D, 100D),
    SICK("CD003","아픔", 100D, 100D, 100D, 10D, 100D),
    ;

    private final String code;
    private final String message;
    private final Double weightPercent;
    private final Double strengthPercent;
    private final Double satietyPercent;
    private final Double healthyPercent;
    private final Double sleepPercent;
}