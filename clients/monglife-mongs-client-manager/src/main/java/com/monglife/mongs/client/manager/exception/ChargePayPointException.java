package com.monglife.mongs.client.manager.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.manager.enums.ManagerResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class ChargePayPointException extends ErrorException {

    public ChargePayPointException(Long mongId) {
        this.response = ManagerResponse.CLIENT_MANAGER_CHARGE_PAY_POINT_FAIL;
        this.result = Collections.singletonMap("mongId", mongId);
    }
}
