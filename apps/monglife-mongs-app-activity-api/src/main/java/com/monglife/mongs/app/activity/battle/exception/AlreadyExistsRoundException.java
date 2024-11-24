package com.monglife.mongs.app.activity.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.activity.global.enums.ActivityResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class AlreadyExistsRoundException extends ErrorException {

    public AlreadyExistsRoundException(Integer round, String playerId) {
        this.response = ActivityResponse.ACTIVITY_BATTLE_ALREADY_EXISTS_ROUND;
        this.result = Map.of("round", round, "playerId", playerId);
    }
}
