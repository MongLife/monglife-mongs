package com.monglife.mongs.app.activity.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.activity.global.enums.ActivityResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsPlayerIdException extends ErrorException {

    public NotExistsPlayerIdException(String playerId) {
        this.response = ActivityResponse.ACTIVITY_BATTLE_NOT_EXISTS_PLAYER_ID;
        this.result = Collections.singletonMap("playerId", playerId);
    }
}
