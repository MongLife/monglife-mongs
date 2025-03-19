package com.monglife.mongs.domain.match.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.match.enums.MatchResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsRoomIdException extends ErrorException {

    public NotExistsRoomIdException(Long roomId) {
        this.response = MatchResponse.DOMAIN_MATCH_NOT_EXISTS_ROOM_ID;
        this.result = Collections.singletonMap("roomId", roomId);
    }
}
