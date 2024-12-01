package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class NotExistsMongException extends ErrorException {

    public NotExistsMongException(Long accountId, Long mongId) {
        this.response = MongResponse.DOMAIN_MONG_NOT_EXISTS_MONG;
        this.result = Map.of("accountId", accountId, "mongId", mongId);
    }
}
