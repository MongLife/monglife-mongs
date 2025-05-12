package com.monglife.mongs.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BotMatchMongType {

    CH100("CH100", "별몽"),
    CH101("CH101", "동글몽"),
    CH102("CH102", "네몽")
    ;

    private final String mongTypeCode;

    private final String mongTypeName;
}
