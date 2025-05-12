package com.monglife.mongs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchRoundCode {

    /**
     * 선택
     */
    MATCH_PICK_ATTACK("배틀 공격 선택"),
    MATCH_PICK_DEFENCE("배틀 방어 선택"),
    MATCH_PICK_HEAL("배틀 회복 선택"),

    /**
     * 선택 완료
     */
    MATCH_HISTORY_DEFENCED("배틀 방어 이력"),
    MATCH_HISTORY_ATTACKED("배틀 피해 이력"),
    MATCH_HISTORY_HEALED("배틀 회복 이력"),

    /**
     * 응답
     */
    NONE("원상태 유지"),
    MATCH_DEFENCE("배틀 방어"),
    MATCH_ATTACKED("배틀 피해"),
    MATCH_HEAL("배틀 회복"),
    MATCH_ATTACKED_HEAL("배틀 피해 & 회복"),
    ;

    private final String message;
}
