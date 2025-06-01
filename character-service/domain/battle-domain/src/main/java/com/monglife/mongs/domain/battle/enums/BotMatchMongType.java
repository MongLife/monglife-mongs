package com.monglife.mongs.domain.battle.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotMatchMongType {

    CH100("CH100", "별몽"),
    CH101("CH101", "동글몽"),
    CH102("CH102", "네몽")
    ;

    private final String mongCode;

    private final String mongName;
}
