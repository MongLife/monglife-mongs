package com.monglife.mongs.domain.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.battle.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

@Getter
public class OnlyBotMatchingException extends ErrorException {

    public OnlyBotMatchingException(Set<String> playerIds) {
        this.response = BattleResponse.DOMAIN_BATTLE_ONLY_BOT_MATCHING;
        this.result = Collections.singletonMap("playerIds", playerIds);
    }
}
