package com.mongs.management.domain.mong.service.dto;

import com.mongs.management.exception.ManagementErrorCode;
//import com.mongs.core.code.MongActiveCode;
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
public record Stroke (
    String mongCode,
    String message,
    String stateCode,
    double weight,
    double health,
    double strength,
    double sleep,
    int poopCount
){
    public static Stroke of(Mong mong){
        return Stroke.builder()
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
//                .message(MongActiveCode.STROKE.getName())
//                .stateCode(MongActiveCode.STROKE.getCode())
                .weight(mong.getWeight())
                .health(mong.getHealthy())
                .strength(mong.getStrength())
                .poopCount(mong.getNumberOfPoop())
                .build();
    }
}
