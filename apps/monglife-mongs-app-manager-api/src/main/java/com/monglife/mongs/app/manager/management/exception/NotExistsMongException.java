package com.monglife.mongs.app.manager.management.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.manager.global.enums.ManagerResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class NotExistsMongException extends ErrorException {

    public NotExistsMongException(Long accountId, Long mongId) {
        this.response = ManagerResponse.MANAGER_MANAGEMENT_NOT_EXISTS_MONG;
        this.result = Map.of("accountId", accountId, "mongId", mongId);
    }
}
