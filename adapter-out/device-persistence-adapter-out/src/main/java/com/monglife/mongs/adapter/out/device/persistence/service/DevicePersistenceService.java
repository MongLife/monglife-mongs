package com.monglife.mongs.adapter.out.device.persistence.service;

import com.monglife.mongs.application.device.domain.Step;
import com.monglife.mongs.application.device.port.out.DevicePersistencePort;
import com.monglife.mongs.application.device.port.vo.CreateStepVo;
import org.springframework.stereotype.Component;

@Component
public class DevicePersistenceService implements DevicePersistencePort {

    @Override
    public void createStepPort(CreateStepVo createStepVo) {

    }

    @Override
    public void decreaseWalkingCountPort(Long deviceId, Integer decreaseWalkingCount) {

    }

    @Override
    public void updateWalkingCountPort(Step step) {

    }

    @Override
    public void increaseWalkingCountPort(Long deviceId, Integer increaseWalkingCount) {

    }
}
