package com.monglife.mongs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchHistoryCode {
    MATCH_HISTORY_DEFENCED("배틀 방어 이력"),
    MATCH_HISTORY_ATTACKED("배틀 피해 이력"),
    MATCH_HISTORY_HEALED("배틀 회복 이력"),
    ;

    private final String message;
}
