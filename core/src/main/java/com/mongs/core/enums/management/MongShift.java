package com.mongs.core.enums.management;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongShift {

    NORMAL("SH000","정상"),
    DEAD("SH001","죽음"),
    GRADUATE_READY("SH002","졸업 대기"),
    EVOLUTION_READY("SH003","진화 대기"),
    DELETE("SH999","삭제"),
    EMPTY("SH444","없음"),
    ;

    private final String code;
    private final String message;
}