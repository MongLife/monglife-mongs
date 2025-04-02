package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidGraduateException extends ErrorException {

    public InvalidGraduateException(Long mongId) {
        this.response = MongResponse.DOMAIN_MONG_INVALID_GRADUATE;
        this.result = Map.of("mongId", mongId);
    }
}
