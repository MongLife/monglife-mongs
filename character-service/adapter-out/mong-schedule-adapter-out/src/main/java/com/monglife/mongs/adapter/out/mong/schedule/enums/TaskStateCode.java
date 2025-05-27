package com.monglife.mongs.adapter.out.mong.schedule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStateCode {

    PROCESSING("task 진행중"),
    PAUSE("task 일시 중지"),
    APP_STOP_PROCESSING("task 진행중, app 중지"),
    APP_STOP_PAUSE("task 일시 중지, app 중지"),
    ;

    private final String description;
}
