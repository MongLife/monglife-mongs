package com.mongs.core.code.enums.management;

import com.mongs.core.code.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongGrade implements Code {

    ZERO("GD000", "알", "ZERO"),
    FIRST("GD001", "1단계", "FIRST"),
    SECOND("GD002", "2단계", "SECOND"),
    THIRD("GD003", "3단계", "THIRD"),
    LAST("GD004", "졸업", "LAST");

    private final String code;
    private final String name;
    private final String value;
}