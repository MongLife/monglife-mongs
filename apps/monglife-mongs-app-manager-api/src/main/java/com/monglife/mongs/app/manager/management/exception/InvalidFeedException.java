package com.monglife.mongs.app.manager.management.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.manager.global.enums.ManagerResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class InvalidFeedException extends ErrorException {

    public InvalidFeedException(Long mongId, String foodTypeCode, LocalDateTime expiration) {
        this.response = ManagerResponse.MANAGER_MANAGEMENT_INVALID_FEED;
        this.result = Map.of("mongId", mongId, "foodTypeCode", foodTypeCode, "expirationAt", expiration);
    }
}
