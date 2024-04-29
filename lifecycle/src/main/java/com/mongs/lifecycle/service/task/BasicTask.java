package com.mongs.lifecycle.service.task;

import com.mongs.core.enums.lifecycle.TaskStatusCode;

public interface BasicTask {
    void start();
    void pause(TaskStatusCode taskStatusCode);
    void stop();
}
