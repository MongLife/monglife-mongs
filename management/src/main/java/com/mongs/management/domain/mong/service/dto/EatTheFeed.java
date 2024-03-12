package com.mongs.management.domain.mong.service.dto;

import com.mongs.core.entity.FoodCode;
import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.management.domain.mong.entity.Mong;
import lombok.*;

/**
 * code: String
 * message: String
 * stateCode: String
 * weight: Integer
 * health: Double
 * satiety: Double
 * strength: Double
 * sleep: Boolean
 * poopCount: Integer
 */

@Builder
public record EatTheFeed (
    String mongCode,
    String message,
    String stateCode,
    double weight,
    double health,
    double strength,
    Boolean sleep,
    int poopCount
){
    public static EatTheFeed of(Mong mong, FoodCode foodCode){
        return EatTheFeed.builder()
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
                .message(foodCode.code())
                .stateCode(foodCode.code())
                .weight(mong.getWeight())
                .health(mong.getHealthy())
                .strength(mong.getStrength())
                .sleep(mong.getIsSleeping())
                .poopCount(mong.getNumberOfPoop())
                .build();
    }
}
