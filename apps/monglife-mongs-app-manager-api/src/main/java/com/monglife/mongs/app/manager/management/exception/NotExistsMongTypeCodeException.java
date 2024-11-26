package com.monglife.mongs.app.manager.management.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.manager.global.enums.ManagerResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsMongTypeCodeException extends ErrorException {

    public NotExistsMongTypeCodeException() {
        this.response = ManagerResponse.MANAGER_MANAGEMENT_NOT_EXISTS_MONG_TYPE;
        this.result = Collections.emptyMap();
    }
}
