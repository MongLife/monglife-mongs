package com.mongs.lifecycle.thread;

import com.mongs.lifecycle.entity.MongEvent;

public interface BasicTask {
    MongEvent getEvent();
    void start();
    void stop();
}
