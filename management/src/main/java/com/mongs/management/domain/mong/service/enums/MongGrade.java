package com.mongs.management.domain.mong.service.enums;

import com.mongs.core.code.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongGrade implements Code {

    ZERO("GD000", "알"),
    FIRST("GD001", "1단계"),
    SECOND("GD002", "2단계"),
    THIRD("GD003", "3단계"),
    LAST("GD004", "졸업");

    private final String groupCode = "GD";
    private final String code;
    private final String name;
}