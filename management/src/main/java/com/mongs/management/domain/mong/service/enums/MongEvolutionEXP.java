package com.mongs.management.domain.mong.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongEvolutionEXP {

    FIRST_GRADE("100", "FIRST"),
    SECOND_GRADE("300", "SECOND"),
    THIRD_GRADE("500", "THIRD"),
    FOURTH_GRADE("800", "LAST");

    private final String exp;
    private final String name;
}
