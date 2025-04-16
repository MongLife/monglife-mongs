package com.monglife.mongs.application.device.port.out;

public interface DeviceEventPort {

    void exchangeCurrentWalkingCountEventPort(Long mongId, Integer walkingCount, Integer payPoint);
}
