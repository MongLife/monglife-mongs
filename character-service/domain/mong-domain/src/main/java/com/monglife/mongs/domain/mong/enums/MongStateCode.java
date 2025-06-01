package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongStateCode {

    NORMAL("정상"),
    EVOLUTION_READY("진화 대기"),
    GRADUATE_READY("졸업 대기"),
    DEAD("죽음"),
    GRADUATE("졸업")
    ;

    public final String description;
}