package com.monglife.mongs.adapter.out.mong.schedule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTypeCode {

    FIX_TIME("시간 고정 한번 실행"),
    FIX_TIME_CYCLE("시간 고정 반복 실행"),
    NONE_FIX_TIME("일정 시간 이후 한번 실행"),
    NONE_FIX_TIME_CYCLE("일정 시간 간격 반복 실행"),
    ;

    private final String message;
}
