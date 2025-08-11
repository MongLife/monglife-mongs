package com.monglife.mongs.domain.battle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotMatchMongType {

    CH100("CH100", "별몽", 50D, 50D, 50D),
    CH101("CH101", "동글몽", 50D, 50D, 50D),
    CH102("CH102", "네몽", 50D, 50D, 50D),
    CH200("CH200", "안씻은 별별몽", 150D, 150D, 150D),
    CH201("CH201", "안씻은 동글몽", 150D, 150D, 150D),
    CH202("CH202", "안씻은 나네몽", 150D, 150D, 150D),
    CH210("CH210", "무난한 별별몽", 250D, 250D, 250D),
    CH211("CH211", "무난한 동글몽", 250D, 250D, 250D),
    CH212("CH212", "무난한 나네몽", 250D, 250D, 250D),
    ;

    private final String mongCode;

    private final String mongName;

    private final Double strength;

    private final Double fatigue;

    private final Double weight;
}
