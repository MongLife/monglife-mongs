package com.monglife.mongs.domain.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.battle.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongIdException extends ErrorException {

    public NotExistsMongIdException(Long mongId) {
        this.response = BattleResponse.DOMAIN_BATTLE_NOT_EXISTS_MONG_ID;
        this.result = Collections.singletonMap("mongId", mongId);
    }
}
