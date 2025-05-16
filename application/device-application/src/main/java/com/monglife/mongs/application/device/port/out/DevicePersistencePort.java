package com.monglife.mongs.application.device.port.out;

import com.monglife.mongs.application.device.port.out.vo.CreateStepVo;
import com.monglife.mongs.domain.device.model.Step;

import java.util.Optional;

public interface DevicePersistencePort {

    Step createStepPort(CreateStepVo createStepVo);

    Optional<Step> saveStepPort(Step step);

    Optional<Step> getStepPort(String deviceId);
}
