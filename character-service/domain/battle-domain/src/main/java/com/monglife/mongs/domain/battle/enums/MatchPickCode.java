package com.monglife.mongs.domain.battle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchPickCode {

    MATCH_PICK_ATTACK("배틀 공격 선택"),
    MATCH_PICK_DEFENCE("배틀 방어 선택"),
    MATCH_PICK_HEAL("배틀 회복 선택"),
    ;

    private final String message;
}
