package com.mongs.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupCode {
    MAP("MP"),
    ACTIVE("AT"),
    MONG("CH"),
    CONDITION("CD");

    private final String groupCode;
}
