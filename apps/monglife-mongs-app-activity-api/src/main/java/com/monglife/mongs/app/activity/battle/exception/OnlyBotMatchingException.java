package com.monglife.mongs.app.activity.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.activity.global.enums.ActivityResponse;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

@Getter
public class OnlyBotMatchingException extends ErrorException {

    public OnlyBotMatchingException(Set<String> playerIds) {
        this.response = ActivityResponse.ACTIVITY_BATTLE_ONLY_BOT_MATCHING;
        this.result = Collections.singletonMap("playerIds", playerIds);
    }
}
