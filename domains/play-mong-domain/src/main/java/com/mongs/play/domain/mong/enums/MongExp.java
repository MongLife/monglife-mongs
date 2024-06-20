package com.mongs.play.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MongExp {

    STROKE(1D),
    ATTENDANCE(5D),
    CLEANING_POOP(2D),
    EAT_THE_FOOD(3D)
    ;

    public final Double exp;
}
