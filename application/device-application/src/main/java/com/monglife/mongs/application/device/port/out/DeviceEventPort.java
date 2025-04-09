package com.monglife.mongs.application.device.port.out;

import com.monglife.mongs.application.device.domain.Step;

public interface DeviceEventPort {

    void exchangeWalkingCountEventPort(Step step);
}
