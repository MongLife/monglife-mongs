package com.monglife.mongs.app.activity.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.activity.global.enums.ActivityResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsWaitMatchingException extends ErrorException {

    public NotExistsWaitMatchingException() {
        this.response = ActivityResponse.ACTIVITY_BATTLE_NOT_EXISTS_WAIT_MATCHING;
        this.result = Collections.emptyMap();
    }
}
