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
public record Poop (
    String mongCode,
    String message,
    String stateCode,
    double weight,
    double health,
    double strength,
    Boolean isSleeping,
    int poopCount
){
    public static Poop of(Mong mong){
        return Poop.builder()
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
//                .message(MongActiveCode.BOWEL.getName())
//                .stateCode(MongActiveCode.BOWEL.getCode())
                .weight(mong.getWeight())
                .health(mong.getHealthy())
                .strength(mong.getStrength())
                .isSleeping(mong.getIsSleeping())
                .poopCount(mong.getNumberOfPoop())
                .build();
    }
}
