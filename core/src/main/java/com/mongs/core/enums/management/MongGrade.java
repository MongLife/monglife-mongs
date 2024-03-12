package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongGrade {

    FOURTH("GD004", "졸업", 800, null),
    THIRD("GD003", "3단계", 500, MongGrade.FOURTH),
    SECOND("GD002", "2단계", 300, MongGrade.THIRD),
    FIRST("GD001", "1단계", 100, MongGrade.SECOND),
    ZERO("GD000", "알", 0, MongGrade.FIRST),
    ;

    private final String code;
    private final String name;
    private final Integer evolutionExp;
    private final MongGrade nextGrade;
}