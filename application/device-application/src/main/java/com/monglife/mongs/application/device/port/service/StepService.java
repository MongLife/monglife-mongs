package com.monglife.mongs.application.device.port.service;

import com.monglife.mongs.application.device.port.command.ExchangeWalkingCountCommand;
import com.monglife.mongs.application.device.port.command.IncreaseWalkingCountCommand;
import com.monglife.mongs.application.device.port.command.UpdateWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.StepUseCase;
import com.monglife.mongs.application.device.port.out.DeviceEventPort;
import com.monglife.mongs.application.device.port.out.DevicePersistencePort;
import com.monglife.mongs.application.device.port.out.DevicePublishPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StepService implements StepUseCase {

    private final DeviceEventPort deviceEventPort;

    private final DevicePersistencePort devicePersistencePort;

    private final DevicePublishPort devicePublishPort;

    @Override
    public void exchangeWalkingCountUseCase(ExchangeWalkingCountCommand command) {

    }

    @Override
    public void updateWalkingCountUseCase(UpdateWalkingCountCommand command) {

    }

    @Override
    public void IncreaseWalkingCountUseCase(IncreaseWalkingCountCommand command) {

    }
}
