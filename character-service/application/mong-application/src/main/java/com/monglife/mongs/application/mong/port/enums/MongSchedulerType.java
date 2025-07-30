package com.monglife.mongs.application.mong.port.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongSchedulerType implements SchedulerType {

    EGG_EVOLUTION("EGG-EVOLUTION", 300L),
    SLEEP("SLEEP", 86400L),
    WAKEUP("WAKEUP", 86400L),
    INCREASE_STATUS("INCREASE-STATUS", 900L),
    DECREASE_STATUS("DECREASE-STATUS", 900L),
    INCREASE_POOP("INCREASE-POOP", 3600L),
    DEAD("DEAD", 43200L),
    ;

    private final String code;

    private final Long expiration;
}
