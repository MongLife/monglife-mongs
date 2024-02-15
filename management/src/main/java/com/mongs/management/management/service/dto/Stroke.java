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
    public Stroke of(Management management){
        return Stroke.builder()
                .mongCode(ManagementStateCode.SUCCESS.getCode())
                .message(MongActiveCode.STROKE.getMessage())
                .stateCode(MongActiveCode.STROKE.getCode())
                .weight(management.getWeight())
                .health(management.getHealthy())
                .strength(management.getStrength())
                .sleep(management.getSleep())
                .poopCount(management.getPoopCount())
                .build();
    }
}
