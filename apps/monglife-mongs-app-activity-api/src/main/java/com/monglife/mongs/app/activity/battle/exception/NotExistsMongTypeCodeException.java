package com.monglife.mongs.app.activity.battle.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.activity.global.enums.ActivityResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongTypeCodeException extends ErrorException {

    public NotExistsMongTypeCodeException() {
        this.response = ActivityResponse.ACTIVITY_BATTLE_NOT_EXISTS_MONG_TYPE;
        this.result = Collections.emptyMap();
    }
}
