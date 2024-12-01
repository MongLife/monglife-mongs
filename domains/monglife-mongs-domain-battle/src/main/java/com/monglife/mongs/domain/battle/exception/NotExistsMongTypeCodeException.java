package com.monglife.mongs.domain.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.battle.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongTypeCodeException extends ErrorException {

    public NotExistsMongTypeCodeException() {
        this.response = BattleResponse.DOMAIN_BATTLE_NOT_EXISTS_MONG_TYPE;
        this.result = Collections.emptyMap();
    }
}
