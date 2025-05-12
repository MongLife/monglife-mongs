package com.monglife.mongs.application.mong.port.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongSchedulerTypeCode {

    EGG_EVOLUTION("EGG-EVOLUTION", 300L, Boolean.FALSE),
    SLEEP("SLEEP", 86400L, Boolean.TRUE),
    WAKEUP("WAKEUP", 86400L, Boolean.TRUE),
    INCREASE_STATUS("INCREASE-STATUS", 900L, Boolean.FALSE),
    DECREASE_STATUS("DECREASE-STATUS", 900L, Boolean.FALSE),
    INCREASE_POOP("INCREASE-POOP", 7200L, Boolean.FALSE),
    DEAD("DEAD", 43200L, Boolean.FALSE),
    ;

    private final String code;

    private final Long expiration;

    private final Boolean isFixedTime;
}
