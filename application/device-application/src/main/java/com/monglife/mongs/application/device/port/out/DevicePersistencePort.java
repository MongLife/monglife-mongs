package com.monglife.mongs.application.device.port.out;

import com.monglife.mongs.domain.model.Step;
import com.monglife.mongs.application.device.port.out.vo.CreateStepVo;

import java.util.Optional;

public interface DevicePersistencePort {

    Step createStepPort(CreateStepVo createStepVo);

    void saveStepPort(Step step);

    Optional<Step> getStepPort(String deviceId);
}
