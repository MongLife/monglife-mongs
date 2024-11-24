package com.monglife.mongs.app.activity.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.activity.global.enums.ActivityResponse;
import com.monglife.mongs.app.activity.battle.enums.BattleStateCode;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsBattleStateCodeException extends ErrorException {

    public NotExistsBattleStateCodeException(BattleStateCode battleRoundCode) {
        this.response = ActivityResponse.ACTIVITY_BATTLE_NOT_EXISTS_BATTLE_ROUND_CODE_RESPONSE;
        this.result = Collections.singletonMap("code", battleRoundCode.name());
    }
}
