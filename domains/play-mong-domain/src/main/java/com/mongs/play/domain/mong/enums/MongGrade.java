package com.mongs.play.domain.mong.enums;

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

    END("GD006", "졸업 완료", 0D, 0D, null),
    LAST("GD005", "졸업 준비", 500D, 1000D, MongGrade.END),
    THIRD("GD004", "3단계", 300D, 500D, MongGrade.LAST),
    SECOND("GD003", "2단계", 100D, 300D, MongGrade.THIRD),
    FIRST("GD002", "1단계", 0D, 100D, MongGrade.SECOND),
    ZERO("GD001", "알", 0D, 100D, MongGrade.FIRST),
    EMPTY("GD000", "없음",0D, 0D, MongGrade.ZERO)
    ;

    public final String code;
    public final String name;
    public final Double evolutionExp;
    public final Double maxStatus;
    public final MongGrade nextGrade;
}