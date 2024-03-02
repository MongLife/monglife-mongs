package com.mongs.core.code.enums.management;

import com.mongs.core.code.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongShift implements Code {

    NORMAL("SH000","정상"),
    DIE("SH001","죽음"),
    GRADUATE("SH002","졸업"),
    EVOLUTION_READY("SH003","진화대기"),
    EMPTY("SH444","없음"),
    DELETE("SH999","삭제")
    ;

    private final String code;
    private final String name;
}