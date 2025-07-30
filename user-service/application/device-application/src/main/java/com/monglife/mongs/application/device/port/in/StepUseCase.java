package com.monglife.mongs.application.device.port.in;

import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.GetStepCommand;
import com.monglife.mongs.application.device.port.in.command.IncreaseCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.UpdateTotalWalkingCountCommand;
import com.monglife.mongs.domain.device.model.Step;

public interface StepUseCase {

    Step getStepUseCase(GetStepCommand command);

    Step exchangeCurrentWalkingCountUseCase(ExchangeCurrentWalkingCountCommand command);

    Step updateTotalWalkingCountUseCase(UpdateTotalWalkingCountCommand command);

    Step increaseCurrentWalkingCountUseCase(IncreaseCurrentWalkingCountCommand command);
}
