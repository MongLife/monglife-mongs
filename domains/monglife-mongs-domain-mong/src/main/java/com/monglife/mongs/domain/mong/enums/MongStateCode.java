package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongStateCode {

    NORMAL("정상"),
    GRADUATE_READY("졸업 대기"),
    EVOLUTION_READY("진화 대기"),
    DEAD("죽음"),
    ;

    public final String name;
}