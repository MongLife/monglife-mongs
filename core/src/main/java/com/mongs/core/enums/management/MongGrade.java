package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongGrade {

    GRADUATE("GD005", "졸업", 500, null),
    THIRD("GD004", "3단계", 300, MongGrade.GRADUATE),
    SECOND("GD003", "2단계", 100, MongGrade.THIRD),
    FIRST("GD002", "1단계", 0, MongGrade.SECOND),
    ZERO("GD001", "알", 0, MongGrade.FIRST),
    EMPTY("GD000", "없음",0, MongGrade.ZERO)
    ;

    private final String code;
    private final String name;
    private final Integer evolutionExp;
    private final MongGrade nextGrade;
}