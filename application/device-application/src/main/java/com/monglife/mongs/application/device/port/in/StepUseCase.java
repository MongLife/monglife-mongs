package com.monglife.mongs.application.device.port.in;

import com.monglife.mongs.application.device.port.command.ExchangeWalkingCountCommand;
import com.monglife.mongs.application.device.port.command.IncreaseWalkingCountCommand;
import com.monglife.mongs.application.device.port.command.UpdateWalkingCountCommand;

public interface StepUseCase {

    void exchangeWalkingCountUseCase(ExchangeWalkingCountCommand exchangeWalkingCountCommand);

    void updateWalkingCountUseCase(UpdateWalkingCountCommand updateWalkingCountCommand);

    void IncreaseWalkingCountUseCase(IncreaseWalkingCountCommand increaseWalkingCountCommand);
}
