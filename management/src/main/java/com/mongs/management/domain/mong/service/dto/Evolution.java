package com.mongs.management.domain.mong.service.dto;

import com.mongs.management.domain.mong.entity.Mong;
import com.mongs.management.exception.ManagementErrorCode;
import lombok.*;

/**
 * mongCode: String
 * stateCode: String
 * weight: Integer
 */


@Builder
public record Evolution (
    String mongCode,
    String stateCode,
    double weight
){
    public static Evolution of(Mong mong){
        return Evolution.builder()
                .mongCode(mong.getGrade().getCode())
                .stateCode(mong.getMongCondition().getCode())
                .weight(mong.getWeight())
                .build();
    }
}
