package com.monglife.mongs.app.user.player.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.user.player.enums.PlayerResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class AlreadyMaxSlotCountException extends ErrorException {

    public AlreadyMaxSlotCountException(Integer slotCount) {
        this.response = PlayerResponse.APP_USER_PLAYER_ALREADY_MAX_SLOT_COUNT;
        this.result = Collections.singletonMap("slotCount", slotCount);
    }
}
