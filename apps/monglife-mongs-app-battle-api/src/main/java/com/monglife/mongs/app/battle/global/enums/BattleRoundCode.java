package com.monglife.mongs.app.battle.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BattleRoundCode {

    BATTLE_ATTACK("배틀 공격"),
    BATTLE_DEFENCE("배틀 방어"),
    BATTLE_HEAL("배틀 회복"),
    ;

    private final String message;
}
