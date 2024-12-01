package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidMongTypeLevelException extends ErrorException {

    public InvalidMongTypeLevelException(Long mongId, String mongTypeCode, Integer mongTypeLevel) {
        this.response = MongResponse.DOMAIN_MONG_INVALID_MONG_TYPE_LEVEL;
        this.result = Map.of("mongId", mongId, "mongTypeCode", mongTypeCode, "mongTypeLevel", mongTypeLevel);
    }
}
