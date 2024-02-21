package com.mongs.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupCode {
    MAP("MP"),
    MONG("CH");

    private final String groupCode;
}
