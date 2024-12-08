package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongStateHistoryCode {

    HISTORY_MONG_STATE_SET_SLEEP("수면 상태로 변경"),
    HISTORY_MONG_STATE_SET_WAKEUP("수면 상태로 변경"),
    HISTORY_MONG_STATE_SET_CODE("상태 코드 변경"),
    ;

    public final String name;
}