package com.monglife.mongs.domain.match.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.match.enums.MatchResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsWaitMatchingException extends ErrorException {

    public NotExistsWaitMatchingException() {
        this.response = MatchResponse.DOMAIN_MATCH_NOT_EXISTS_WAIT_MATCHING;
        this.result = Collections.emptyMap();
    }
}
