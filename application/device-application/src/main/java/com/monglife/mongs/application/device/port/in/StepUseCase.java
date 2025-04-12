package com.monglife.mongs.application.device.port.in;

import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.IncreaseCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.UpdateTotalWalkingCountCommand;

public interface StepUseCase {

    void exchangeCurrentWalkingCountUseCase(ExchangeCurrentWalkingCountCommand exchangeCurrentWalkingCountCommand);

    void updateTotalWalkingCountUseCase(UpdateTotalWalkingCountCommand updateTotalWalkingCountCommand);

    void increaseCurrentWalkingCountUseCase(IncreaseCurrentWalkingCountCommand increaseCurrentWalkingCountCommand);
}
