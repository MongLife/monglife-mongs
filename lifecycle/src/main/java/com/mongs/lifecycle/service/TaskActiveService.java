package com.mongs.lifecycle.service;

import com.mongs.lifecycle.entity.MongEvent;

public interface TaskActiveService {
    void decreaseWeight(MongEvent event) throws RuntimeException;
    void decreaseStrength(MongEvent event) throws RuntimeException;
    double decreaseSatiety(MongEvent event) throws RuntimeException;
    double decreaseHealthy(MongEvent event) throws RuntimeException;
    void decreaseSleep(MongEvent event) throws RuntimeException;
    void increaseSleep(MongEvent event) throws RuntimeException;
    void increasePayPoint(MongEvent event) throws RuntimeException;
    void increasePoop(MongEvent event) throws RuntimeException;
    void dead(MongEvent event) throws RuntimeException;
}
