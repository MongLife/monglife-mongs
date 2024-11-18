package com.monglife.mongs.app.battle.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BattleStateCode {

    /**
     * 요청
     */
    BATTLE_ENTER("배틀 입장"),
    BATTLE_EXIT("배틀 퇴장"),
    BATTLE_PICK("배틀 선택"),

    /**
     * 응답
     */
    BATTLE_CREATE("배틀 생성"),
    BATTLE_OVER("배틀 종료"),
    ;

    private final String message;
}
