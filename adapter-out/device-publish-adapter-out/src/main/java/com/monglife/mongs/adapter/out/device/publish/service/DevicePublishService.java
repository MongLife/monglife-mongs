package com.monglife.mongs.adapter.out.device.publish.service;

import com.monglife.mongs.application.device.domain.Step;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import org.springframework.stereotype.Component;

@Component
public class DevicePublishService implements DevicePublishPort {

    @Override
    public void publishStepWalkingCountPort(Step step) {

    }
}
