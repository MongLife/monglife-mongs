package com.monglife.mongs.domain.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.battle.enums.BattleResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class AlreadyExistsRoundException extends ErrorException {

    public AlreadyExistsRoundException(Integer round, String playerId) {
        this.response = BattleResponse.DOMAIN_BATTLE_ALREADY_EXISTS_ROUND;
        this.result = Map.of("round", round, "playerId", playerId);
    }
}
