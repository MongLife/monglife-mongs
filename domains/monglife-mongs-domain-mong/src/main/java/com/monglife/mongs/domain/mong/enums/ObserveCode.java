package com.monglife.mongs.domain.mong.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ObserveCode {

    MANAGEMENT_MONG("몽 정보에 변화가 있습니다.", "management"),
    MANAGEMENT_MONG_STATE("몽 상태 정보에 변화가 있습니다.", "management"),
    MANAGEMENT_MONG_STATUS("몽 지수 정보에 변화가 있습니다.", "management"),
    ;

    public final String message;

    private final String subTopic;
}