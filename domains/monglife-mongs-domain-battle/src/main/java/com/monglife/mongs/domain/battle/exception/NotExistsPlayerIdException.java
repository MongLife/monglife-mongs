package com.monglife.mongs.domain.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.battle.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsPlayerIdException extends ErrorException {

    public NotExistsPlayerIdException(String playerId) {
        this.response = BattleResponse.DOMAIN_BATTLE_NOT_EXISTS_PLAYER_ID;
        this.result = Collections.singletonMap("playerId", playerId);
    }
}
