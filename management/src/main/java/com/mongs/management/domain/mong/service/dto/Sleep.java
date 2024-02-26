package com.mongs.management.domain.mong.service.dto;

import com.mongs.management.exception.ManagementErrorCode;
import com.mongs.core.code.MongActiveCode;
import com.mongs.management.domain.mong.entity.Management;
import lombok.*;


@Builder(toBuilder = true)
public record Sleep (
    String mongCode,
    String message,
    String stateCode,
    double weight,
    double health,
    double strength,
    double sleep,
    int poopCount
){
    public static Sleep of(Management management){
        return Sleep.builder()
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
                .message(MongActiveCode.SLEEP.getName())
                .stateCode(MongActiveCode.SLEEP.getCode())
                .weight(management.getWeight())
                .health(management.getHealthy())
                .strength(management.getStrength())
                .poopCount(management.getPoopCount())
                .build();
    }
}