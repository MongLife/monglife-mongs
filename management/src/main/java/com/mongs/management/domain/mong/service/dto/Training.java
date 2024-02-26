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
 * sleep: Double
 * poopCount: Integer
 */
@Builder
public record Training (
    String mongCode,
    String message,
    String stateCode,
    double weight,
    double health,
    double strength,
    Boolean isSleep,
    int poopCount
){
    public static Training of(Mong mong){
        return Training.builder()
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
                .message(MongActiveCode.TRAINING.getName())
                .stateCode(MongActiveCode.TRAINING.getCode())
                .weight(mong.getWeight())
                .health(mong.getHealthy())
                .strength(mong.getStrength())
                .isSleep(mong.getIsSleeping())
                .poopCount(mong.getNumberOfPoop())
                .build();
    }
}
