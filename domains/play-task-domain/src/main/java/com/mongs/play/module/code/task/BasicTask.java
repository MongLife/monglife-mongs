package com.mongs.play.module.code.task;

import com.mongs.play.module.code.enums.TaskStatusCode;

public interface BasicTask {
    void start();
    void pause(TaskStatusCode taskStatusCode);
    void stop();
}
