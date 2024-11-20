package com.monglife.mongs.app.battle.global.exception;

import com.monglife.core.enums.response.Response;
import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.battle.global.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class OnlyBotMatchingException extends RuntimeException implements ErrorException {

    private final Response response = BattleResponse.BATTLE_ONLY_BOT_MATCHING;

    private final Map<String, Set<String>> result;

    public OnlyBotMatchingException(Set<String> playerIds) {
        this.result = Collections.singletonMap("playerIds", playerIds);
    }
}
