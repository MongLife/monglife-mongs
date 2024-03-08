package com.mongs.lifecycle.task;

import com.mongs.lifecycle.entity.MongEvent;

public interface BasicTask {
    MongEvent getEvent();
    void start();
    void stop();
}
