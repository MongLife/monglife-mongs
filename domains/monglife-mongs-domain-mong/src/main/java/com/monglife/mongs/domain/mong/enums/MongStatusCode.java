package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongStatusCode {

    SOMNOLENCE("졸림"),
    HUNGRY("배고픔"),
    SICK("아픔"),
    NORMAL("정상"),
    ;

    private final String name;
}