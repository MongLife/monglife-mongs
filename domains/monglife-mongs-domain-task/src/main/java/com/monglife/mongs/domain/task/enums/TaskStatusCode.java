package com.monglife.mongs.domain.task.enums;

import lombok.Getter;

@Getter
public enum TaskStatusCode {

    PROCESSING("task 진행중"),
    PAUSE("task 일시 중지"),

    APP_STOP_PROCESSING("task 진행중, app 중지"),
    APP_STOP_PAUSE("task 일시 중지, app 중지"),
    ;

    private final String message;

    TaskStatusCode(String message) {
        this.message = message;
    }
}
