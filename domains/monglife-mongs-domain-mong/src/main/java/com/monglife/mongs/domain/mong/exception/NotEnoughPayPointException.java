package com.monglife.mongs.domain.mong.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.domain.mong.enums.MongResponse;
import lombok.Getter;

import java.util.Map;

@Getter
public class NotEnoughPayPointException extends ErrorException {

    public NotEnoughPayPointException(Long mongId, String foodTypeCode, Integer price, Integer payPoint) {
        this.response = MongResponse.DOMAIN_MONG_NOT_ENOUGH_PAY_POINT;
        this.result = Map.of("mongId", mongId, "foodTypeCode", foodTypeCode, "price", price, "payPoint", payPoint);
    }
}
