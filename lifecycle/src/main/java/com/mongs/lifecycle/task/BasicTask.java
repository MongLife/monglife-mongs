package com.mongs.lifecycle.task;

import com.mongs.lifecycle.code.TaskStatusCode;

public interface BasicTask {
    void start();
    void pause(TaskStatusCode taskStatusCode);
    void stop();
}
