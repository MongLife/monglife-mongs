package com.mongs.play.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MongExp {

    STROKE(5),
    ATTENDANCE(5),
    CLEANING_POOP(1),
    EAT_THE_FOOD(10)
    ;

    public final Integer exp;
}
