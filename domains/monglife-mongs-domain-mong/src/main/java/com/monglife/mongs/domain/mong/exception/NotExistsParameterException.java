package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class NotExistsParameterException extends ErrorException {

    public NotExistsParameterException(String parameterName) {
        this.response = MongResponse.DOMAIN_MONG_NOT_EXISTS_PARAMETER;
        this.result = Map.of("parameterName", parameterName);
    }
}
