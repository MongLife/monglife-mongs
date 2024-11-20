package com.monglife.mongs.app.battle.global.exception;

import com.monglife.core.enums.response.Response;
import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.battle.global.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class NotExistsPlayerIdException extends RuntimeException implements ErrorException {

    private final Response response = BattleResponse.BATTLE_NOT_EXISTS_PLAYER_ID;

    private final Map<String, Object> result;

    public NotExistsPlayerIdException(String playerId) {
        this.result = Collections.singletonMap("playerId", playerId);
    }
}
