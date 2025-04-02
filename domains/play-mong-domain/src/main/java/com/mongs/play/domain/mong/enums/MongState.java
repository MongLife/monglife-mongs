package com.mongs.play.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MongState {

    SOMNOLENCE("CD001","졸림", 0D, 0D, 0D, 0D, 10D),
    HUNGRY("CD002","배고픔", 0D, 0D, 10D, 0D, 0D),
    SICK("CD003","아픔", 0D, 0D, 0D, 10D, 0D),
    NORMAL("CD000","정상", 0D, 0D, 0D, 0D, 0D),
    EMPTY("CD444","없음", 0D, 0D, 0D, 0D, 0D),
    ;

    public final String code;
    public final String message;
    public final Double weightPercent;
    public final Double strengthPercent;
    public final Double satietyPercent;
    public final Double healthyPercent;
    public final Double sleepPercent;
}