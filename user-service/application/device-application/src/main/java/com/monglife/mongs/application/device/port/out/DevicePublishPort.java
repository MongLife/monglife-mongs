package com.monglife.mongs.application.device.port.out;

import com.monglife.mongs.domain.device.model.Step;

public interface DevicePublishPort {

    void publishCurrentWalkingCountPort(Step step);
}
