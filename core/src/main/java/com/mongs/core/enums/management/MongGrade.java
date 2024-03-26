package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongGrade {

    LAST("GD005", "졸업", 500, 1000D, null),
    THIRD("GD004", "3단계", 300, 500D, MongGrade.LAST),
    SECOND("GD003", "2단계", 100, 300D, MongGrade.THIRD),
    FIRST("GD002", "1단계", 0, 100D, MongGrade.SECOND),
    ZERO("GD001", "알", 0, 100D, MongGrade.FIRST),
    EMPTY("GD000", "없음",0, 0D, MongGrade.ZERO)
    ;

    private final String code;
    private final String name;
    private final Integer evolutionExp;
    private final Double maxStatus;
    private final MongGrade nextGrade;
}