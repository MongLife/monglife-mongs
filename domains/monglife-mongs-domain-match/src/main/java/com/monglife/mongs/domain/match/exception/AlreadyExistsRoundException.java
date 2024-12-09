package com.monglife.mongs.domain.match.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.match.enums.MatchResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class AlreadyExistsRoundException extends ErrorException {

    public AlreadyExistsRoundException(Integer round, String playerId) {
        this.response = MatchResponse.DOMAIN_MATCH_ALREADY_EXISTS_ROUND;
        this.result = Map.of("round", round, "playerId", playerId);
    }
}
