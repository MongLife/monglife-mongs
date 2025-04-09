package com.monglife.mongs.application.member.port.service;

import com.monglife.mongs.application.member.domain.Player;
import com.monglife.mongs.application.member.port.command.*;
import com.monglife.mongs.application.member.port.in.PlayerUseCase;
import com.monglife.mongs.application.member.port.out.GooglePaymentPort;
import com.monglife.mongs.application.member.port.out.MemberPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService implements PlayerUseCase {

    private final MemberPersistencePort memberPersistencePort;

    private final GooglePaymentPort googlePaymentPort;

    @Override
    public void createPlayerUseCase(CreatePlayerCommand createPlayerCommand) {

    }

    @Override
    public Player getPlayerUseCase(GetPlayerCommand getPlayerCommand) {
        return null;
    }

    @Override
    public void buySlotUseCase(BuySlotCommand buySlotCommand) {

    }

    @Override
    public void exchangeStarPointUseCase(ExchangeStarPointCommand exchangeStarPointCommand) {

    }

    @Override
    public void increaseStarPointUseCase(IncreaseStarPointCommand increaseStarPointCommand) {

    }
}
