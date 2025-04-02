package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class NotExistsFoodTypeCodeException extends ErrorException {

    public NotExistsFoodTypeCodeException(String foodTypeCode) {
        this.response = MongResponse.DOMAIN_MONG_NOT_EXISTS_FOOD_TYPE;
        this.result = Collections.singletonMap("foodTypeCode", foodTypeCode);
    }
}
