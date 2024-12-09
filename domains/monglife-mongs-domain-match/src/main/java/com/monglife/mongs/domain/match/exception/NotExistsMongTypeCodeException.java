package com.monglife.mongs.domain.match.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.match.enums.MatchResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongTypeCodeException extends ErrorException {

    public NotExistsMongTypeCodeException() {
        this.response = MatchResponse.DOMAIN_MATCH_NOT_EXISTS_MONG_TYPE;
        this.result = Collections.emptyMap();
    }
}
