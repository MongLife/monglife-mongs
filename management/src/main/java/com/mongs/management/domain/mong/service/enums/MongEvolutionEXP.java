package com.mongs.management.domain.mong.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongEvolutionEXP {

    FIRST_GRADE("100", "1단계"),
    SECOND_GRADE("300", "2단계"),
    THIRD_GRADE("500", "3단계"),
    FOURTH_GRADE("800", "졸업");

    private final String exp;
    private final String name;
}
