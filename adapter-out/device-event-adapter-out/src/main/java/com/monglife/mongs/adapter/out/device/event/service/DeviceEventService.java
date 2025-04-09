package com.monglife.mongs.adapter.out.device.event.service;

import com.monglife.mongs.application.device.domain.Step;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import org.springframework.stereotype.Component;

@Component
public class DeviceEventService implements DeviceEventPort {

    @Override
    public void exchangeWalkingCountEventPort(Step step) {

    }
}
