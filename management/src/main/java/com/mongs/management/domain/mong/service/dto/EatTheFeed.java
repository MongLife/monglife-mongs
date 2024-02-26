package com.mongs.management.domain.mong.service.dto;

import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.core.code.MongActiveCode;
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
    public EatTheFeed of(Mong mong){
        return EatTheFeed.builder()
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
                .message(MongActiveCode.MEAL.getName())
                .stateCode(MongActiveCode.MEAL.getCode())
                .weight(mong.getWeight())
                .health(mong.getHealthy())
                .strength(mong.getStrength())
                .sleep(mong.getIsSleeping())
                .poopCount(mong.getNumberOfPoop())
                .build();
    }
}
