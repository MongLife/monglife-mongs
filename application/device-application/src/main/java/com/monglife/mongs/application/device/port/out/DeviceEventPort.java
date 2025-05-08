package com.monglife.mongs.application.device.port.out;

public interface DeviceEventPort {

    void exchangeCurrentWalkingCountEventPort(String deviceId, Long mongId, Integer walkingCount, Integer payPoint);
}
