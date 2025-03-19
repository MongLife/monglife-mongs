package com.monglife.mongs.client.manager.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.manager.enums.ManagerResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class GetMinimalMongException extends ErrorException {

    public GetMinimalMongException(Long mongId) {
        this.response = ManagerResponse.CLIENT_MANAGER_GET_MINIMAL_MONG_FAIL;
        this.result = Collections.singletonMap("mongId", mongId);
    }
}
