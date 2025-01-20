package com.monglife.mongs.app.activity.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.activity.battle.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class NotExistsMongException extends ErrorException {

    public NotExistsMongException(Long mongId) {
        this.response = BattleResponse.APP_ACTIVITY_BATTLE_NOT_EXISTS_MONG;
        this.result = Map.of("mongId", mongId);
    }
}
