package com.mongs.management.service.dto;

import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.core.code.MongActiveCode;
import com.mongs.management.entity.Management;
import lombok.*;

/**
 * code: String
 * message: String
 * stateCode: String
 * weight: Integer
 * health: Double
 * satiety: Double
 * strength: Double
 * sleep: Double
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
    double sleep,
    int poopCount
){
    public EatTheFeed of(Management management){
        return EatTheFeed.builder()
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
                .message(MongActiveCode.MEAL.getName())
                .stateCode(MongActiveCode.MEAL.getCode())
                .weight(management.getWeight())
                .health(management.getHealthy())
                .strength(management.getStrength())
                .sleep(management.getSleep())
                .poopCount(management.getPoopCount())
                .build();
    }
}
