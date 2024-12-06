package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class InvalidMongStateException extends ErrorException {

    public InvalidMongStateException() {
        this.response = MongResponse.DOMAIN_MONG_INVALID_MONG_STATE;
        this.result = Collections.emptyMap();
    }
}
