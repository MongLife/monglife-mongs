package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongExp {

    STROKE(5, "5EXP per stroke"),
    ATTENDANCE(5, "5EXP per attendance"),
    CLEANING_POOP(1, "1EXP per attendance"),
    EAT_THE_FOOD(10, "5EXP per attendance")
    ;

    private final Integer exp;
    private final String name;
}
