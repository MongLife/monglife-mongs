package com.mongs.play.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MongExp {

    STROKE(2D),
    ATTENDANCE(5D),
    CLEANING_POOP(2.5D),
    EAT_THE_FOOD(5D)
    ;

    public final Double exp;
}
