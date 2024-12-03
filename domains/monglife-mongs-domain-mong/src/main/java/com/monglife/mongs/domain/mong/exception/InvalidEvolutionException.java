package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidEvolutionException extends ErrorException {

    public InvalidEvolutionException(Long mongId) {
        this.response = MongResponse.DOMAIN_MONG_INVALID_EVOLUTION;
        this.result = Map.of("mongId", mongId);
    }
}
