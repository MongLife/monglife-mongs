package com.monglife.mongs.app.management.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * LAST : 졸업 준비 상태
 * THIRD : 3단계
 * SECOND : 2단계
 * FIRST : 1단계
 * ZERO : 0단계 (알)
 * EMPTY : 없음
 */

@AllArgsConstructor
public enum MongGrade {

    END("GD006", "졸업 완료", Double.MAX_VALUE, Double.MAX_VALUE, null, -1),
    LAST("GD005", "졸업 준비", Double.MAX_VALUE, Double.MAX_VALUE, MongGrade.END, -1),
    THIRD("GD004", "3단계", 500D, 500D, MongGrade.LAST, 3),
    SECOND("GD003", "2단계", 300D, 300D, MongGrade.THIRD, 2),
    FIRST("GD002", "1단계", 100D, 100D, MongGrade.SECOND, 1),
    ZERO("GD001", "알", 0D, 100D, MongGrade.FIRST, 0),
    EMPTY("GD000", "없음",0D, 0D, MongGrade.ZERO, -1)
    ;

    public final String code;
    public final String name;
    public final Double evolutionExp;
    public final Double maxStatus;
    public final MongGrade nextGrade;
    public final Integer level;
}