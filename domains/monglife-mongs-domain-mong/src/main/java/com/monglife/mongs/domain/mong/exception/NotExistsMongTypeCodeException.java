package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongTypeCodeException extends ErrorException {

    public NotExistsMongTypeCodeException() {
        this.response = MongResponse.DOMAIN_MONG_NOT_EXISTS_MONG_TYPE;
        this.result = Collections.emptyMap();
    }
}
