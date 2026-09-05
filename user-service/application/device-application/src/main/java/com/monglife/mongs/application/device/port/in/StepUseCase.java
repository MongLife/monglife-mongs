package com.monglife.mongs.application.device.port.in;

import com.monglife.mongs.application.device.port.in.command.ExchangeCurrentWalkingCountCommand;
import com.monglife.mongs.application.device.port.in.command.RestoreExchangedWalkingCountCommand;
import com.monglife.mongs.domain.device.model.Step;

public interface StepUseCase {

    Step exchangeCurrentWalkingCountUseCase(ExchangeCurrentWalkingCountCommand command);

    void restoreExchangedWalkingCountUseCase(RestoreExchangedWalkingCountCommand command);
}
