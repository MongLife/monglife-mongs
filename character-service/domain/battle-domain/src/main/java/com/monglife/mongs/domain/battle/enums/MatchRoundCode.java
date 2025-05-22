package com.monglife.mongs.domain.battle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchRoundCode {

    NONE("원상태 유지"),
    MATCH_DEFENCE("배틀 방어"),
    MATCH_ATTACKED("배틀 피해"),
    MATCH_HEAL("배틀 회복"),
    MATCH_ATTACKED_HEAL("배틀 피해 & 회복"),
    ;

    private final String message;
}
