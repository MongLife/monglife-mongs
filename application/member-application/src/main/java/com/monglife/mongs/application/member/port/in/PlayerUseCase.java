package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.*;
import com.monglife.mongs.domain.member.model.Player;

public interface PlayerUseCase {

    void createPlayerUseCase(CreatePlayerCommand command);

    Player getPlayerUseCase(GetPlayerCommand command);

    Player buySlotUseCase(BuySlotCommand command);

    Player exchangeStarPointUseCase(ExchangeStarPointCommand command);

    Player increaseStarPointUseCase(IncreaseStarPointCommand command);
}
