package com.mongs.core.enums.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlotCountCode {
    NORMAL(1),
    SPECIAL(3),
    ADMIN(100),
    ;

    private final Integer count;
}
