package com.monglife.mongs.app.activity.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMatchException extends ErrorException {

    public NotExistsMatchException(Long roomId) {
        this.response = BattleResponse.APP_ACTIVITY_BATTLE_NOT_EXISTS_OVER_MATCH;
        this.result = Collections.singletonMap("roomId", roomId);
    }
}
