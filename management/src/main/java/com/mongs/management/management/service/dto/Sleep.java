package com.mongs.management.management.service.dto;

import com.mongs.common.ManagementStateCode;
import com.mongs.common.MongActiveCode;
import com.mongs.management.management.entity.Management;
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
    public Sleep of(Management management){
        return Sleep.builder()
                .mongCode(ManagementStateCode.SUCCESS.getCode())
                .message(MongActiveCode.SLEEP.getMessage())
                .stateCode(MongActiveCode.SLEEP.getCode())
                .weight(management.getWeight())
                .health(management.getHealthy())
                .strength(management.getStrength())
                .sleep(management.getSleep())
                .poopCount(management.getPoopCount())
                .build();
    }
}