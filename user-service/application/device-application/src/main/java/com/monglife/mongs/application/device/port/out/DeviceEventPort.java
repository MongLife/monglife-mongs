package com.monglife.mongs.application.device.port.out;

import com.monglife.mongs.application.device.port.out.dto.StepEventDto;

public interface DeviceEventPort {

    void exchangeCurrentWalkingCountEventPort(StepEventDto stepEventDto);
}
