package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongStateHistoryType {

    SET_SLEEP("수면 상태로 변경"),
    SET_WAKEUP("수면 상태로 변경"),
    SET_CODE("상태 코드 변경"),
    ;

    public final String name;
}
