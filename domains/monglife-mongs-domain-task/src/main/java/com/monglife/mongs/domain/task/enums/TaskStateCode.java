package com.monglife.mongs.domain.task.enums;

import lombok.Getter;

@Getter
public enum TaskStateCode {

    FIX_TIME("시간 고정 한번 실행"),
    FIX_TIME_CYCLE("시간 고정 반복 실행"),
    NONE_FIX_TIME("일정 시간 이후 한번 실행"),
    NONE_FIX_TIME_CYCLE("일정 시간 간격 반복 실행"),
    ;

    private final String message;

    TaskStateCode(String message) {
        this.message = message;
    }
}
