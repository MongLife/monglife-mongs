package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class InvalidFeedException extends ErrorException {

    public InvalidFeedException(Long mongId, String foodTypeCode, Long expirationSeconds) {
        this.response = MongResponse.DOMAIN_MONG_INVALID_FEED;
        this.result = Map.of("mongId", mongId, "foodTypeCode", foodTypeCode, "expirationSeconds", expirationSeconds);
    }
}
