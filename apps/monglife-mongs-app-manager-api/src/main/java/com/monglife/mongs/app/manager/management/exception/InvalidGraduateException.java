package com.monglife.mongs.app.manager.management.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.manager.global.enums.ManagerResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidGraduateException extends ErrorException {

    public InvalidGraduateException(Long accountId, Long mongId) {
        this.response = ManagerResponse.MANAGER_MANAGEMENT_INVALID_GRADUATE;
        this.result = Map.of("accountId", accountId, "mongId", mongId);
    }
}
