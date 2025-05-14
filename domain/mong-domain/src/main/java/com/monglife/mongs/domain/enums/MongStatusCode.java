package com.monglife.mongs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongStatusCode {

    SICK("아픔"),
    HUNGRY("배고픔"),
    SOMNOLENCE("졸림"),
    NORMAL("정상"),
    ;

    private final String statusName;
}