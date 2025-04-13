package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.*;
import com.monglife.mongs.domain.model.Player;

public interface PlayerUseCase {

    void createPlayerUseCase(CreatePlayerCommand command);

    Player getPlayerUseCase(GetPlayerCommand command);

    void buySlotUseCase(BuySlotCommand command);

    void exchangeStarPointUseCase(ExchangeStarPointCommand command);

    void increaseStarPointUseCase(IncreaseStarPointCommand command);
}
