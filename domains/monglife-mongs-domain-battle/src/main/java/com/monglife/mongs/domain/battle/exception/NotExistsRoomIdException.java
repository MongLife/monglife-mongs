package com.monglife.mongs.domain.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.battle.enums.BattleResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsRoomIdException extends ErrorException {

    public NotExistsRoomIdException(Long roomId) {
        this.response = BattleResponse.DOMAIN_BATTLE_NOT_EXISTS_ROOM_ID;
        this.result = Collections.singletonMap("roomId", roomId);
    }
}
