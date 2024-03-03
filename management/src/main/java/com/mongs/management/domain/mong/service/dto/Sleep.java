package com.mongs.management.domain.mong.service.dto;

import com.mongs.management.exception.ManagementErrorCode;
//import com.mongs.core.code.MongActiveCode;
import com.mongs.management.domain.mong.entity.Mong;
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
    public static Sleep of(Mong mong){
        return Sleep.builder()
                .mongCode(ManagementErrorCode.SUCCESS.getCode())
//                .message(MongActiveCode.SLEEP.getName())
//                .stateCode(MongActiveCode.SLEEP.getCode())
                .weight(mong.getWeight())
                .health(mong.getHealthy())
                .strength(mong.getStrength())
                .poopCount(mong.getNumberOfPoop())
                .build();
    }
}