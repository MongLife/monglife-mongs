package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongTraining {

    JUMPING("TR100", "Jumping Training", 5, 10, 0D, 5D, 0D, 0D, 0D)
    ;

    private final String code;
    private final String name;
    private final Integer point;
    private final Integer exp;
    private final Double addWeightValue;
    private final Double addStrengthValue;
    private final Double addSatietyValue;
    private final Double addHealthyValue;
    private final Double addSleepValue;
}
