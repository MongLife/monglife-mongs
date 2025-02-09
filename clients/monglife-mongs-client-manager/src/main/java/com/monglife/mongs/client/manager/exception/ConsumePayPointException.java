package com.monglife.mongs.client.manager.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.manager.enums.ManagerResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class ConsumePayPointException extends ErrorException {

    public ConsumePayPointException(Long mongId) {
        this.response = ManagerResponse.CLIENT_MANAGER_CONSUME_PAY_POINT_FAIL;
        this.result = Collections.singletonMap("mongId", mongId);
    }
}
