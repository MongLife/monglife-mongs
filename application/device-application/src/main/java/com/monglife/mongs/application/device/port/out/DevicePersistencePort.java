package com.monglife.mongs.application.device.port.out;

import com.monglife.mongs.application.device.domain.Step;
import com.monglife.mongs.application.device.port.vo.CreateStepVo;

public interface DevicePersistencePort {

    void createStepPort(CreateStepVo createStepVo);

    void decreaseWalkingCountPort(Long deviceId, Integer decreaseWalkingCount);

    void updateWalkingCountPort(Step step);

    void increaseWalkingCountPort(Long deviceId, Integer increaseWalkingCount);
}
