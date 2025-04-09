package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.domain.Player;
import com.monglife.mongs.application.member.port.command.*;

public interface PlayerUseCase {

    void createPlayerUseCase(CreatePlayerCommand createPlayerCommand);

    Player getPlayerUseCase(GetPlayerCommand getPlayerCommand);

    void buySlotUseCase(BuySlotCommand buySlotCommand);

    void exchangeStarPointUseCase(ExchangeStarPointCommand exchangeStarPointCommand);

    void increaseStarPointUseCase(IncreaseStarPointCommand increaseStarPointCommand);
}
