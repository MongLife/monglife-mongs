package com.monglife.mongs.app.management.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MongShift {

    NORMAL("SH000","정상"),
    DEAD("SH001","죽음"),
    GRADUATE_READY("SH002","졸업 대기"),
    EVOLUTION_READY("SH003","진화 대기"),
    GRADUATE("SH004", "졸업"),
    EMPTY("SH444","없음"),
    DELETE("SH999","삭제"),
    ;

    public final String code;
    public final String message;
}