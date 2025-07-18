package com.monglife.mongs.application.device.port.out;

import com.monglife.mongs.domain.device.model.Step;

import java.util.Optional;

public interface DeviceReadPort {

    Optional<Step> getStepPort(String deviceId);
}
