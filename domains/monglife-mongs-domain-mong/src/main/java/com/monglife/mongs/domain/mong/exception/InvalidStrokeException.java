package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidStrokeException extends ErrorException {

    public InvalidStrokeException(Long mongId, Long expirationSeconds) {
        this.response = MongResponse.DOMAIN_MONG_INVALID_STROKE;
        this.result = Map.of("mongId", mongId, "expirationSeconds", expirationSeconds);
    }
}
