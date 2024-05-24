package com.mongs.play.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MongExp {

    STROKE(5D),
    ATTENDANCE(5D),
    CLEANING_POOP(1D),
    EAT_THE_FOOD(10D)
    ;

    public final Double exp;
}
