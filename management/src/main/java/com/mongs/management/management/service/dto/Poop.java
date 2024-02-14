package com.mongs.management.management.service.dto;

import com.mongs.common.ManagementStateCode;
import com.mongs.common.MongActiveCode;
import com.mongs.management.management.entity.Management;
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
    double sleep,
    int poopCount
){
    public Poop of(Management management){
        return Poop.builder()
                .mongCode(ManagementStateCode.SUCCESS.getCode())
                .message(MongActiveCode.BOWEL.getMessage())
                .stateCode(MongActiveCode.BOWEL.getCode())
                .weight(management.getWeight())
                .health(management.getHealthy())
                .strength(management.getStrength())
                .sleep(management.getSleep())
                .poopCount(management.getPoopCount())
                .build();
    }
}
