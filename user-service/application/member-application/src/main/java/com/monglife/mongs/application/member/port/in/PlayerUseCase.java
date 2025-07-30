package com.monglife.mongs.application.member.port.in;

import com.monglife.mongs.application.member.port.in.command.*;
import com.monglife.mongs.domain.member.model.Player;

public interface PlayerUseCase {

    /**
     * 플레이어 등록
     */
    void createPlayerUseCase(CreatePlayerCommand command);

    /**
     * 플레이어 조회
     */
    Player getPlayerUseCase(GetPlayerCommand command);

    /**
     * 슬롯 구매
     */
    Player buySlotUseCase(BuySlotCommand command);

    /**
     * 스타 포인트 환전
     */
    Player exchangeStarPointUseCase(ExchangeStarPointCommand command);

    /**
     * 스타 포인트 증가
     */
    Player increaseStarPointUseCase(IncreaseStarPointCommand command);
}
