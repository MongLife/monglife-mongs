package com.monglife.mongs.app.manager.management.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.app.manager.global.enums.ManagerResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsFoodTypeCodeException extends ErrorException {

    public NotExistsFoodTypeCodeException(String foodTypeCode) {
        this.response = ManagerResponse.MANAGER_MANAGEMENT_NOT_EXISTS_FOOD_TYPE;
        this.result = Collections.singletonMap("foodTypeCode", foodTypeCode);
    }
}
