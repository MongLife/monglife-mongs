package com.monglife.mongs.domain.battle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchStateCode {
    ENTERING("매치 플레이어 입장중"),
    PROCESS("매치 진행중"),
    END("매치 종료"),
    ;

    private final String message;
}
