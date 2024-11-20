package com.monglife.mongs.app.battle.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BattleRoundCode {

    NONE("NULL"),
    /**
     * 선택
     */
    BATTLE_PICK_ATTACK("배틀 공격 선택"),
    BATTLE_PICK_DEFENCE("배틀 방어 선택"),
    BATTLE_PICK_HEAL("배틀 회복 선택"),

    /**
     * 선택 완료
     */
    BATTLE_DEFENCE("배틀 방어"),
    BATTLE_DAMAGE("배틀 피해"),
    BATTLE_DEFENCE_HEAL("배틀 피해 & 회복"),
    BATTLE_HEAL("배틀 피해"),
    ;

    private final String message;
}
