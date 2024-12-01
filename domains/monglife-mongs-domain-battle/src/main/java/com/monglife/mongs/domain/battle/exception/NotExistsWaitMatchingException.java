package com.monglife.mongs.domain.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.battle.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsWaitMatchingException extends ErrorException {

    public NotExistsWaitMatchingException() {
        this.response = BattleResponse.DOMAIN_BATTLE_NOT_EXISTS_WAIT_MATCHING;
        this.result = Collections.emptyMap();
    }
}
