package com.monglife.mongs.application.device.port.in;

import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.IncreaseCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.UpdateTotalWalkingCountCommand;
import com.monglife.mongs.domain.model.Step;

public interface StepUseCase {

    Step exchangeCurrentWalkingCountUseCase(ExchangeCurrentWalkingCountCommand exchangeCurrentWalkingCountCommand);

    Step updateTotalWalkingCountUseCase(UpdateTotalWalkingCountCommand updateTotalWalkingCountCommand);

    Step increaseCurrentWalkingCountUseCase(IncreaseCurrentWalkingCountCommand increaseCurrentWalkingCountCommand);
}
