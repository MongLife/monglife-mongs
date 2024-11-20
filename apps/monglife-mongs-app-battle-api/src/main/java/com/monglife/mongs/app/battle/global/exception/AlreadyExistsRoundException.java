package com.monglife.mongs.app.battle.global.exception;

import com.monglife.core.enums.response.Response;
import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.battle.global.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class AlreadyExistsRoundException extends RuntimeException implements ErrorException {

    private final Response response = BattleResponse.BATTLE_ALREADY_EXISTS_ROUND;

    private final Map<String, Object> result;

    public AlreadyExistsRoundException(Integer round, String playerId) {
        this.result = Map.of("round", round, "playerId", playerId);
    }
}
